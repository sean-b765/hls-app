package dev.seanboaden.hls.media;

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
  private MediaScanProgressRegistry mediaScanProgressRegistry;

  @EventListener(ApplicationReadyEvent.class)
  public void startup() {
    mediaScanService.doScan(media -> {
      mediaScanProgressRegistry.updateProgress(media.getId(), MediaProgressEnum.METADATA);
      // Retrieve framerate and duration
      mediaMetadataService.getMetadata(media);
      mediaScanProgressRegistry.updateProgress(media.getId(), MediaProgressEnum.INFO);
      // Retrieve info about the media
      mediaMetadataService.getInfo(media);
      mediaScanProgressRegistry.updateProgress(media.getId(), MediaProgressEnum.READY);
    });
    mediaScanProgressRegistry.clear();
  }
}
