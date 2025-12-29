package dev.seanboaden.hls.collection.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.repository.TvSeasonCollectionRepository;
import dev.seanboaden.hls.config.base.AbstractCrudService;

@Service
public class TvSeasonCollectionService
    extends AbstractCrudService<TvSeasonCollection, String, TvSeasonCollectionRepository> {
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

}
