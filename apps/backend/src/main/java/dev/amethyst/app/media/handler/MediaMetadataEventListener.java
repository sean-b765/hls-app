package dev.amethyst.app.media.handler;

import org.springframework.stereotype.Component;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.media.model.MediaMetadata;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
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
