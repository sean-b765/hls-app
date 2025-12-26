package dev.seanboaden.hls.collection.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.repository.TvSeasonCollectionRepository;
import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaService;
import jakarta.transaction.Transactional;

@Service
public class TvSeasonCollectionService
    extends AbstractCrudService<TvSeasonCollection, String, TvSeasonCollectionRepository> {
  @Autowired
  private MediaService mediaService;

  protected TvSeasonCollectionService(TvSeasonCollectionRepository repository) {
    super(repository);
  }

  public Optional<TvSeasonCollection> findExternalById(String externalId) {
    return this.repository.findByExternalId(externalId);
  }

  public Optional<TvSeasonCollection> findByName(String name) {
    return this.repository.findByName(name);
  }

  public Optional<TvSeasonCollection> findByTvSeriesAndSeason(String seriesId, Integer season) {
    return this.repository.findByTvSeriesIdAndSeason(seriesId, season);
  }

  @Transactional
  public void addMediaToTvSeason(String mediaId, String tvSeasonId) {
    if (tvSeasonId == null || mediaId == null)
      return;

    TvSeasonCollection tvSeason = this.repository.findById(tvSeasonId)
        .orElseThrow();
    Media media = this.mediaService.findById(mediaId)
        .orElseThrow();

    // Establish links

    tvSeason.getMediaItems().add(media);
    media.setTvSeason(tvSeason);
    this.mediaService.save(media);
  }
}
