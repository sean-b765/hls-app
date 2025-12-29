package dev.seanboaden.hls.tv.handler;

import org.springframework.stereotype.Component;

import dev.seanboaden.hls.config.service.SpringContextHolder;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.system.event.ResourceUpdatedEvent;
import dev.seanboaden.hls.system.lib.Endpoints;
import dev.seanboaden.hls.tv.model.TvSeason;
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
