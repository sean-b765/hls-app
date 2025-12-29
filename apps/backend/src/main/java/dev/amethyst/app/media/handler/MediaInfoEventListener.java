package dev.amethyst.app.media.handler;

import org.springframework.stereotype.Component;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class MediaInfoEventListener {
  @PostPersist
  public void created(MediaInfo mediaInfo) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(MediaInfo.class.getSimpleName())
        .id(mediaInfo.getMedia().getId())
        .uri(Endpoints.MEDIA)
        .build();
    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}