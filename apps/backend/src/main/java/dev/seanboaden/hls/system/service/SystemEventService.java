package dev.seanboaden.hls.system.service;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.system.model.SystemEvent;
import dev.seanboaden.hls.system.model.SystemEventType;
import dev.seanboaden.hls.system.repository.SystemEventRepository;
import dev.seanboaden.hls.tv.model.TvSeason;
import dev.seanboaden.hls.tv.model.TvSeries;

@Service
public class SystemEventService extends AbstractCrudService<SystemEvent, String, SystemEventRepository> {
  protected SystemEventService(SystemEventRepository repository) {
    super(repository);
  }

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
