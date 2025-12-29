package dev.amethyst.app.media.handler;

import org.springframework.scheduling.annotation.Async;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.config.web.AsyncModifier;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.media.service.MediaInfoService;
import dev.amethyst.app.media.service.MediaMetadataService;
import dev.amethyst.app.media.service.MediaService;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

public class MediaEventListener {
  @PostPersist
  @Async(AsyncModifier.Modifier.SQLITE)
  public void created(Media media) {
    // Broadcast new media available
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(Media.class.getSimpleName())
        .id(media.getId())
        .uri(Endpoints.MEDIA)
        .build();

    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    MediaMetadataService mediaMetadataService = SpringContextHolder.getBean(MediaMetadataService.class);
    MediaService mediaService = SpringContextHolder.getBean(MediaService.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);

    mediaMetadataService.createMetadata(media);
    mediaService.ensureInfo(media.getId());
  }

  @PostRemove
  public void removed(Media media) {
    System.out.println("DELETE INFO AND META FOR " + media.getId());

  }
}
