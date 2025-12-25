package dev.seanboaden.hls.library.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class LibraryEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;

  @PostPersist
  public void created(Library library) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Library.class.getSimpleName())
        .id(library.getId())
        .uri(Endpoints.LIBRARY)
        .build();
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
