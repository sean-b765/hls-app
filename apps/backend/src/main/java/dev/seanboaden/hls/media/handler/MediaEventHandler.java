package dev.seanboaden.hls.media.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.web.AsyncExecutor;
import dev.seanboaden.hls.media.enums.MediaProgressEnum;
import dev.seanboaden.hls.media.event.MediaCreatedEvent;
import dev.seanboaden.hls.media.service.MediaInfoService;
import dev.seanboaden.hls.media.service.MediaMetadataService;
import dev.seanboaden.hls.media.service.MediaScanProgressRegistry;

@Component
public class MediaEventHandler {
  @Autowired
  private MediaMetadataService mediaMetadataService;
  @Autowired
  private MediaInfoService mediaInfoService;
  @Autowired
  private MediaScanProgressRegistry mediaScanProgressRegistry;

  /**
   * 1. retrieve MediaMetadata
   * 2. retrieve MediaInfo
   * 
   * @param event
   */
  @EventListener
  @Async(AsyncExecutor.Names.SQLITE)
  public void onCreateEvent(MediaCreatedEvent event) {
    if (event == null || event.getMedia() == null)
      return;

    String mediaId = event.getMedia().getId();

    mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.METADATA);
    this.mediaMetadataService.getMetadata(event.getMedia());

    mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.INFO);
    this.mediaInfoService.getMediaInfo(event.getMedia());

    mediaScanProgressRegistry.updateProgress(mediaId, MediaProgressEnum.READY);
  }
}
