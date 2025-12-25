package dev.seanboaden.hls.media.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class MediaInfoEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;

  @PostPersist
  public void created(MediaInfo mediaInfo) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(MediaInfo.class.getSimpleName())
        .id(mediaInfo.getMedia().getId())
        .uri(Endpoints.MEDIA)
        .build();
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}