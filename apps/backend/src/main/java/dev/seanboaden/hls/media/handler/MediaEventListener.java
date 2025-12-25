package dev.seanboaden.hls.media.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class MediaEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;

  @PostPersist
  public void created(Media media) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Media.class.getSimpleName())
        .id(media.getId())
        .uri(Endpoints.MEDIA)
        .build();
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
