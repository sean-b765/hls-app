package dev.seanboaden.hls.library.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.media.service.MediaService;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

@Component
public class LibraryEventListener {
  @PostPersist
  public void created(Library library) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Library.class.getSimpleName())
        .id(library.getId())
        .uri(Endpoints.LIBRARY)
        .build();
    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }

  @PostRemove
  public void removed(Library library) {
    MediaService mediaService = SpringContextHolder.getBean(MediaService.class);
    mediaService.deleteAllByLibraryId(library.getId());
  }
}
