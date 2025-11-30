package dev.seanboaden.hls.video;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.lib.FileSystemService;
import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.media.MediaRepository;
import dev.seanboaden.hls.room.Room;
import dev.seanboaden.hls.room.RoomManager;
import dev.seanboaden.hls.session.SessionRegistry;
import dev.seanboaden.hls.session.SessionWrapper;
import dev.seanboaden.hls.video.QualityProfiles.QualityProfile;
import dev.seanboaden.hls.video.TranscodeJob.JobType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
public class HlsVideoController {
  @Value("${transcode.dir}")
  private String transcodeDumpDirectory;
  @Autowired
  private MediaRepository mediaRepository;
  @Autowired
  private TranscodeManager transcodingManager;
  @Autowired
  private FileSystemService fileSystemService;
  @Autowired
  private SessionRegistry sessionRegistry;
  @Autowired
  private RoomManager roomManager;

  @GetMapping("/{mediaId}/{qualityProfile}/{segmentName}")
  public ResponseEntity<byte[]> getVideoSegment(
      @PathVariable String mediaId,
      @PathVariable String qualityProfile,
      @PathVariable String segmentName,
      @RequestParam(required = false) String userId) {
    Optional<Media> media = mediaRepository.findById(mediaId);
    if (media.isEmpty())
      return ResponseEntity.notFound().build();

    QualityProfile quality = QualityProfiles.findByName(qualityProfile);
    if (quality == null)
      return ResponseEntity.notFound().build();

    // Attempt to retrieve roomId.
    // It's okay if this is unassigned,
    // we will share the media segments between rooms anyways
    SessionWrapper sessionWrapper = sessionRegistry.getByUserId(userId);
    String roomCode = "unassigned";
    if (sessionWrapper != null) {
      Room room = roomManager.findRoomBySession(sessionWrapper);
      roomCode = room.getCode();
    }

    // Start transcoding
    TranscodeJob transcodeJob = TranscodeJob.builder()
        .fromSegmentName(segmentName)
        .roomCode(roomCode)
        .media(media.get())
        .quality(quality)
        .type(JobType.HLS)
        .build();

    String segmentPath = this.fileSystemService.getSegmentDirectory(transcodeJob);

    Path path = Paths.get(segmentPath, segmentName);
    if (!Files.exists(path)) {
      this.transcodingManager.startOrRetrieveWorker(transcodeJob).join();
      return ResponseEntity.notFound().build();
    }

    try {
      byte[] data = Files.readAllBytes(path);
      return ResponseEntity.ok()
          .header("Content-Type", "video/mp2t")
          .header("Cache-Control", "public, max-age=31536000")
          .body(data);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
  }
}
