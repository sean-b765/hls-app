package dev.seanboaden.hls.collection.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class TvSeasonCollectionEventListener {
  @PostPersist
  public void created(TvSeasonCollection tvSeason) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(TvSeasonCollection.class.getSimpleName())
        .id(tvSeason.getId())
        .uri(Endpoints.TV_SEASONS)
        .build();

    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
