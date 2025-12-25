package dev.seanboaden.hls.collection.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class TvSeasonCollectionEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;

  @PostPersist
  public void created(TvSeasonCollection tvSeason) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(TvSeasonCollection.class.getSimpleName())
        .id(tvSeason.getId())
        .uri(Endpoints.TV_SEASONS)
        .build();
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
