package dev.amethyst.app.media.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import dev.amethyst.app.config.event.CreationEvent;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.service.MediaService;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;

@Component
public class MediaEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;
  @Autowired
  private MediaService mediaService;

  @TransactionalEventListener
  public void created(CreationEvent<?> creationEvent) {
    Object payload = creationEvent.getPayload();
    if (!(payload instanceof Media))
      return;

    Media media = (Media) payload;
    // Broadcast new media available
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Media.class.getSimpleName())
        .id(media.getId())
        .uri(Endpoints.MEDIA)
        .build();

    this.sessionRegistry.broadcastResourceUpdatedEvent(event);

    this.mediaService.ensureMetadata(media.getId());
    this.mediaService.ensureInfo(media.getId());
  }
}
