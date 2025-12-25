package dev.seanboaden.hls.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.system.model.SystemEvent;
import dev.seanboaden.hls.system.model.SystemEventType;
import dev.seanboaden.hls.system.repository.SystemEventRepository;

@Service
public class SystemEventService {
  @Autowired
  private SystemEventRepository systemEventRepository;

  public void record(MediaInfo mediaInfo) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(mediaInfo.getMedia().getId())
        .type(SystemEventType.MEDIA_INFO)
        .message("Media info fetched: " + mediaInfo.getName())
        .build();

    systemEventRepository.save(event);
  }

  public void record(TvSeriesCollection tvSeries) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(tvSeries.getId())
        .type(SystemEventType.TV_SERIES)
        .message("TV Series created: " + tvSeries.getName())
        .build();

    systemEventRepository.save(event);
  }

  public void record(TvSeasonCollection tvSeason) {
    SystemEvent event = SystemEvent
        .builder()
        .resourceId(tvSeason.getId())
        .type(SystemEventType.TV_SEASON)
        .message("TV Season created: " + tvSeason.getName())
        .build();

    systemEventRepository.save(event);
  }
}
