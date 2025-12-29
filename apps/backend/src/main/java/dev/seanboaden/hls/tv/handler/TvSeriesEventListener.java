package dev.seanboaden.hls.tv.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import dev.seanboaden.hls.tv.model.TvSeries;
import jakarta.persistence.PostPersist;

@Component
public class TvSeriesEventListener {
  @PostPersist
  public void created(TvSeries tvSeries) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(TvSeries.class.getSimpleName())
        .id(tvSeries.getId())
        .uri(Endpoints.TV_SERIES)
        .build();
    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
