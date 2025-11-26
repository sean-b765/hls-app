package dev.seanboaden.hls.video;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.media.MediaRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
public class HlsVideoController {
  @Value("${transcode.dir}")
  private String transcodeDumpDirectory;
  @Autowired
  private MediaRepository mediaRepository;

  @GetMapping("/{mediaId}/{qualityProfile}/{segmentName}")
  public ResponseEntity<byte[]> getVideoSegment(
      @PathVariable String mediaId,
      @PathVariable String qualityProfile,
      @PathVariable String segmentName) {
    Optional<Media> media = mediaRepository.findById(mediaId);
    if (media.isEmpty())
      return ResponseEntity.notFound().build();

    QualityProfiles.QualityProfile profile = QualityProfiles.findByName(qualityProfile);
    if (profile == null)
      return ResponseEntity.notFound().build();

    // transcode entrypoint
    String segmentPath = StringUtils.joinWith("/", transcodeDumpDirectory, mediaId, profile.getName(), segmentName);
    Path path = Path.of(segmentPath);
    if (!Files.exists(path))
      return ResponseEntity.notFound().build();

    try {
      byte[] data = Files.readAllBytes(path);
      return ResponseEntity.ok()
          .header("Content-Type", "video/mp2t")
          .header("Cache-Control", "public, max-age=31536000")
          .body(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
