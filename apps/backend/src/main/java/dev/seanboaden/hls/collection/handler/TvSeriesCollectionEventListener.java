package dev.seanboaden.hls.collection.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import jakarta.persistence.PostPersist;

@Component
public class TvSeriesCollectionEventListener {
  @Autowired
  private SessionRegistry sessionRegistry;

  @PostPersist
  public void created(TvSeriesCollection tvSeries) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(TvSeriesCollection.class.getSimpleName())
        .id(tvSeries.getId())
        .uri(Endpoints.TV_SERIES)
        .build();
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
