package dev.seanboaden.hls.media;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupMediaService {
  @Autowired
  private MediaScanService mediaScanService;
  @Autowired
  private MediaMetadataService mediaMetadataService;
  @Autowired
  private MediaInfoService mediaInfoService;
  @Autowired
  private MediaScanProgressRegistry mediaScanProgressRegistry;

  @EventListener(ApplicationReadyEvent.class)
  public void startup() {
    mediaScanService.doScan(media -> {
      mediaScanProgressRegistry.updateProgress(media.getId(),
          MediaProgressEnum.METADATA);
      // Retrieve framerate and duration
      mediaMetadataService.getMetadata(media);
      mediaScanProgressRegistry.updateProgress(media.getId(),
          MediaProgressEnum.INFO);
      // Retrieve info about the media
      mediaInfoService.retrieveInfoAndEstablishTvSeriesSeason(media);
      mediaScanProgressRegistry.updateProgress(media.getId(),
          MediaProgressEnum.READY);
    });

    // Wait 3 seconds before clear
    Executors.newSingleThreadScheduledExecutor()
        .schedule(() -> mediaScanProgressRegistry.clear(), 3, TimeUnit.SECONDS);
  }
}
