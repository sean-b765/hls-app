package dev.amethyst.app.playlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.amethyst.app.config.scope.UserRequestScope;
import dev.amethyst.app.config.service.JwtService;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.service.MediaService;
import dev.amethyst.app.playlist.service.PlaylistManager;
import dev.amethyst.app.video.model.QualityProfiles;

import java.util.Optional;

@RestController
@RequestMapping("/api/playlist")
public class HlsPlaylistController {
  @Autowired
  private PlaylistManager playlistManager;
  @Autowired
  private MediaService mediaService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserRequestScope userRequestScope;

  @PostMapping("/{mediaId}")
  public ResponseEntity<?> generateHlsToken(@PathVariable String mediaId) {
    if (this.userRequestScope.getUser() == null)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    String hlsToken = this.jwtService.generateHlsToken(mediaId, this.userRequestScope.getUser());
    return ResponseEntity.ok()
        .header("X-Hls-Token", hlsToken)
        .build();
  }

  @GetMapping("/{mediaId}")
  public ResponseEntity<?> generateMultiVariantPlaylist(@PathVariable String mediaId) {
    if (this.userRequestScope.getUser() == null)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    Optional<Media> media = this.mediaService.findById(mediaId);
    if (media.isEmpty())
      return ResponseEntity.notFound().build();

    String multiVariantPlaylist = this.playlistManager.generateMultiVariantPlaylist(media.get());
    String hlsToken = this.jwtService.generateHlsToken(mediaId, this.userRequestScope.getUser());

    return ResponseEntity.ok()
        .header("Content-Type", "application/vnd.apple.mpegurl")
        .header("X-Hls-Token", hlsToken)
        .body(multiVariantPlaylist);
  }

  @GetMapping("/{mediaId}/{qualityProfile}")
  public ResponseEntity<?> generateVodPlaylist(
      @PathVariable String mediaId,
      @PathVariable String qualityProfile,
      @RequestHeader("X-Hls-Token") String hlsToken) {
    if (this.userRequestScope.getUser() == null)
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    if (this.jwtService.isTokenExpired(hlsToken))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    Optional<Media> media = this.mediaService.findById(mediaId);
    if (media.isEmpty())
      return ResponseEntity.notFound().build();

    QualityProfiles.QualityProfile profile = QualityProfiles.findByName(qualityProfile);
    if (media.isEmpty() || profile == null)
      return ResponseEntity.notFound().build();

    String vodPlaylist = this.playlistManager.generateVodPlaylist(media.get(), profile);

    return ResponseEntity.ok()
        .header("Content-Type", "application/vnd.apple.mpegurl")
        .body(vodPlaylist);
  }
}
