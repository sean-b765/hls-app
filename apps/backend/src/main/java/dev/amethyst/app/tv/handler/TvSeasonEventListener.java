package dev.amethyst.app.tv.handler;

import org.springframework.stereotype.Component;

import dev.amethyst.app.config.service.SpringContextHolder;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.system.event.ResourceUpdatedEvent;
import dev.amethyst.app.system.lib.Endpoints;
import dev.amethyst.app.tv.model.TvSeason;
import jakarta.persistence.PostPersist;

@Component
public class TvSeasonEventListener {
  @PostPersist
  public void created(TvSeason tvSeason) {
    ResourceUpdatedEvent event = ResourceUpdatedEvent.builder()
        .kind(TvSeason.class.getSimpleName())
        .id(tvSeason.getId())
        .uri(Endpoints.TV_SEASONS)
        .build();

    SessionRegistry sessionRegistry = SpringContextHolder.getBean(SessionRegistry.class);
    sessionRegistry.broadcastResourceUpdatedEvent(event);
  }
}
