package dev.seanboaden.hls.media.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
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