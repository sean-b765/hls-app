package dev.seanboaden.hls.media.controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.enums.MediaProgressEnum;
import dev.seanboaden.hls.media.model.MediaMetadata;
import dev.seanboaden.hls.media.service.MediaInfoService;
import dev.seanboaden.hls.media.service.MediaMetadataService;
import dev.seanboaden.hls.media.service.MediaScanProgressRegistry;
import dev.seanboaden.hls.media.service.MediaScanService;

@RestController
@RequestMapping("/api/metadata")
public class MediaMetadataController {
  @Autowired
  private MediaMetadataService mediaMetadataService;
  @Autowired
  private MediaScanService mediaScanService;
  @Autowired
  private MediaInfoService mediaInfoService;
  @Autowired
  private MediaScanProgressRegistry mediaScanProgressRegistry;

  @PostMapping(path = "/scan")
  public ResponseEntity<?> scan() {
    this.mediaScanService.doScan(media -> {
      String mediaId = media.getId();
      this.mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.METADATA);
      // Retrieve framerate and duration
      this.mediaMetadataService.getMetadata(media);
      this.mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.INFO);
      // Retrieve info about the media
      this.mediaInfoService.retrieveInfoAndEstablishTvSeriesSeason(media);
      this.mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.READY);
    });

    // Wait 3 seconds before clear
    Executors.newSingleThreadScheduledExecutor().schedule(() -> mediaScanProgressRegistry.clear(), 3, TimeUnit.SECONDS);

    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<MediaMetadata>> getAll() {
    List<MediaMetadata> theMedias = mediaMetadataService.findAll();
    return ResponseEntity.ok(theMedias);
  }
}
