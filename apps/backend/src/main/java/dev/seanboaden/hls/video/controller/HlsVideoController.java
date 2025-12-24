package dev.seanboaden.hls.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaService;
import dev.seanboaden.hls.room.model.Room;
import dev.seanboaden.hls.room.service.RoomManager;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.session.service.SessionWrapper;
import dev.seanboaden.hls.transcode.model.TranscodeJob;
import dev.seanboaden.hls.transcode.model.TranscodeJob.JobType;
import dev.seanboaden.hls.transcode.service.TranscodeManager;
import dev.seanboaden.hls.video.model.QualityProfiles;
import dev.seanboaden.hls.video.model.QualityProfiles.QualityProfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/video")
public class HlsVideoController {
  @Value("${transcode.dir}")
  private String transcodeDumpDirectory;
  @Autowired
  private MediaService mediaService;
  @Autowired
  private TranscodeManager transcodingManager;
  @Autowired
  private dev.seanboaden.hls.filesystem.service.FileSystemService fileSystemService;
  @Autowired
  private SessionRegistry sessionRegistry;
  @Autowired
  private RoomManager roomManager;

  @GetMapping("/{mediaId}/{qualityProfile}/{segmentName}")
  public DeferredResult<ResponseEntity<byte[]>> getVideoSegment(
      @PathVariable String mediaId,
      @PathVariable String qualityProfile,
      @PathVariable String segmentName,
      @RequestParam(required = false) String userId) {
    DeferredResult<ResponseEntity<byte[]>> deferredResult = new DeferredResult<>(5000L,
        ResponseEntity.status(503).build());
    Optional<Media> media = this.mediaService.findById(mediaId);
    if (media.isEmpty() || media.get().getInfo() == null)
      deferredResult.setResult(ResponseEntity.notFound().build());

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
        .media(media.get())
        .quality(quality)
        .type(JobType.HLS)
        .build();

    Path segmentPath = this.fileSystemService.getSegmentPath(transcodeJob);

    CompletableFuture<Void> segmentReadyFuture = new CompletableFuture<>();

    if (!Files.exists(segmentPath)) {
      this.transcodingManager.ensureWorker(transcodeJob);
      segmentReadyFuture = this.transcodingManager.waitForSegment(transcodeJob, 5000L);
    } else {
      segmentReadyFuture.complete(null);
    }

    segmentReadyFuture.whenComplete((result, exception) -> {
      System.out.println("found " + segmentName);
      if (exception != null) {
        deferredResult.setResult(ResponseEntity.internalServerError().build());
      }

      try {
        byte[] data = Files.readAllBytes(segmentPath);
        deferredResult.setResult(ResponseEntity.ok()
            .header("Content-Type", "video/mp2t")
            .header("Cache-Control", "public, max-age=31536000")
            .body(data));
      } catch (IOException e) {
        System.out.println("\n!!!!!! NOT FOUND: " + transcodeJob.getFromSegmentName());
        deferredResult.setResult(ResponseEntity.internalServerError().build());
      }
    });

    return deferredResult;
  }
}
