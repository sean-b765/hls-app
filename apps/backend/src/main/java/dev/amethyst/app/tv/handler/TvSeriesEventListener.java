package dev.amethyst.app.tv.handler;

import org.springframework.stereotype.Component;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
import dev.amethyst.app.tv.model.TvSeries;
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
