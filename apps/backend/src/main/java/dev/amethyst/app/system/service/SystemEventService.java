package dev.amethyst.app.system.service;

import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.system.model.SystemEvent;
import dev.amethyst.app.system.model.SystemEventType;
import dev.amethyst.app.system.repository.SystemEventRepository;
import dev.amethyst.app.tv.model.TvSeason;
import dev.amethyst.app.tv.model.TvSeries;

@Service
public class SystemEventService extends AbstractCrudService<SystemEvent, String, SystemEventRepository> {
  public void record(MediaInfo mediaInfo) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(mediaInfo.getMedia().getId())
        .type(SystemEventType.MEDIA_INFO)
        .message("Media info fetched: " + mediaInfo.getName())
        .build();

    this.repository.save(event);
  }

  public void record(TvSeries tvSeries) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(tvSeries.getId())
        .type(SystemEventType.TV_SERIES)
        .message("TV Series created: " + tvSeries.getName())
        .build();

    this.repository.save(event);
  }

  public void record(TvSeason tvSeason) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(tvSeason.getId())
        .type(SystemEventType.TV_SEASON)
        .message("TV Season created: " + tvSeason.getName())
        .build();

    this.repository.save(event);
  }
}
