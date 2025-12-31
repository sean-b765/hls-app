package dev.amethyst.app.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import dev.amethyst.app.config.service.JwtService;
import dev.amethyst.app.filesystem.service.FileSystemService;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.service.MediaService;
import dev.amethyst.app.room.model.Room;
import dev.amethyst.app.room.service.RoomManager;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.session.service.SessionWrapper;
import dev.amethyst.app.transcode.model.TranscodeJob;
import dev.amethyst.app.transcode.model.TranscodeJob.JobType;
import dev.amethyst.app.transcode.service.TranscodeManager;
import dev.amethyst.app.video.model.QualityProfiles;
import dev.amethyst.app.video.model.QualityProfiles.QualityProfile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/video")
public class VideoController {
  @Value("${transcode.dir}")
  private String transcodeDumpDirectory;
  @Autowired
  private MediaService mediaService;
  @Autowired
  private TranscodeManager transcodingManager;
  @Autowired
  private FileSystemService fileSystemService;
  @Autowired
  private SessionRegistry sessionRegistry;
  @Autowired
  private RoomManager roomManager;
  @Autowired
  private JwtService jwtService;

  private Map<String, Media> mediaCache = new ConcurrentHashMap<>();

  @GetMapping("/{mediaId}/{qualityProfile}/{segmentName}")
  public DeferredResult<ResponseEntity<Resource>> getVideoSegment(
      @PathVariable String mediaId,
      @PathVariable String qualityProfile,
      @PathVariable String segmentName,
      @RequestHeader("X-Hls-Token") String hlsToken) {
    DeferredResult<ResponseEntity<Resource>> deferredResult = new DeferredResult<>(5000L,
        ResponseEntity.status(503).build());

    if (this.jwtService.isTokenExpired(hlsToken)) {
      deferredResult.setResult(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
      return deferredResult;
    }

    String userId = this.jwtService.extractSubject(hlsToken);
    if (userId == null) {
      deferredResult.setResult(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
      return deferredResult;
    }

    Media media = this.mediaCache.computeIfAbsent(mediaId, id -> this.mediaService.findById(id).orElse(null));
    if (media == null) {
      deferredResult.setResult(ResponseEntity.notFound().build());
      return deferredResult;
    }

    QualityProfile quality = QualityProfiles.findByName(qualityProfile);
    if (quality == null)
      deferredResult.setResult(ResponseEntity.notFound().build());

    // Attempt to retrieve roomId.
    // It's okay if this is unassigned,
    // we will share the media segments between rooms anyways
    SessionWrapper sessionWrapper = sessionRegistry.getByUserId(userId);
    String roomCode = null;
    if (sessionWrapper != null) {
      Room room = roomManager.findRoomBySession(sessionWrapper);
      roomCode = room.getCode();
    }

    TranscodeJob transcodeJob = TranscodeJob.builder()
        .fromSegmentName(segmentName)
        .roomCode(roomCode)
        .userId(userId)
        .media(media)
        .quality(quality)
        .type(JobType.HLS)
        .build();

    Path segmentPath = this.fileSystemService.getSegmentPath(transcodeJob);
    if (segmentPath == null) {
      deferredResult.setResult(ResponseEntity.notFound().build());
      return deferredResult;
    }

    CompletableFuture<Void> segmentReadyFuture = new CompletableFuture<>();
    if (!Files.exists(segmentPath)) {
      this.transcodingManager.ensureWorker(transcodeJob);
      segmentReadyFuture = this.transcodingManager.waitForSegment(transcodeJob, 5000L);
    } else {
      segmentReadyFuture.complete(null);
    }

    segmentReadyFuture.whenComplete((result, exception) -> {
      if (exception != null) {
        deferredResult.setResult(ResponseEntity.internalServerError().build());
        return;
      }

      Resource resource = new FileSystemResource(segmentPath);
      deferredResult.setResult(ResponseEntity.ok()
          .header("Content-Type", "video/mp2t")
          .header("Cache-Control", "public, max-age=31536000")
          .body(resource));
    });

    return deferredResult;
  }
}
