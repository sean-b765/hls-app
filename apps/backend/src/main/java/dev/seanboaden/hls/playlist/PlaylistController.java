package dev.seanboaden.hls.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.media.MediaRepository;
import dev.seanboaden.hls.video.QualityProfiles;

import java.util.Optional;

@RestController()
@RequestMapping("/api/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistManager playlistManager;
    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping("/{mediaId}")
    public ResponseEntity<?> generateMultiVariantPlaylist(@PathVariable long mediaId) {
        Optional<Media> media = mediaRepository.findById(mediaId);
        if (media.isEmpty()) return ResponseEntity.notFound().build();

        String multiVariantPlaylist = playlistManager.generateMultiVariantPlaylist(media.get());
        return ResponseEntity.ok().header("Content-Type", "application/vnd.apple.mpegurl").body(multiVariantPlaylist);
    }

    @GetMapping("/{mediaId}/{qualityProfile}")
    public ResponseEntity<?> generateVodPlaylist(@PathVariable long mediaId, @PathVariable String qualityProfile) {
        Optional<Media> media = mediaRepository.findById(mediaId);
        QualityProfiles.QualityProfile profile = QualityProfiles.findByName(qualityProfile);
        if (media.isEmpty() || profile == null) return ResponseEntity.notFound().build();

        String vodPlaylist = playlistManager.generateVodPlaylist(media.get(), profile);
        return ResponseEntity.ok().header("Content-Type", "application/vnd.apple.mpegurl").body(vodPlaylist);
    }
}
