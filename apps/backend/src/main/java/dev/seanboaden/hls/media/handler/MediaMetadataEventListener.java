package dev.seanboaden.hls.media.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.media.model.MediaMetadata;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class MediaMetadataEventListener {
  @PostPersist
  public void created(MediaMetadata metadata) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(MediaMetadata.class.getSimpleName())
        .id(metadata.getMedia().getId())
        .uri(Endpoints.MEDIA)
        .build();
    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
