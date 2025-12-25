package dev.seanboaden.hls.media.handler;

import org.springframework.scheduling.annotation.Async;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.config.web.AsyncModifier;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaInfoService;
import dev.seanboaden.hls.media.service.MediaMetadataService;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

public class MediaEventListener {
  @PostPersist
  @Async(AsyncModifier.Modifier.SQLITE)
  public void created(Media media) {
    // Broadcast new media available
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Media.class.getSimpleName())
        .id(media.getId())
        .uri(Endpoints.MEDIA)
        .build();

    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    MediaMetadataService mediaMetadataService = SpringContextHolder.getBean(MediaMetadataService.class);
    MediaInfoService mediaInfoService = SpringContextHolder.getBean(MediaInfoService.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);

    mediaMetadataService.createMetadata(media);
    mediaInfoService.createMediaInfo(media);
  }
}
