package dev.amethyst.app.library.handler;

import org.springframework.stereotype.Component;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.library.model.Library;
import dev.amethyst.app.media.service.MediaService;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
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
