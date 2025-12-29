package dev.seanboaden.hls.tv.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.tv.model.TvSeason;
import dev.seanboaden.hls.tv.repository.TvSeasonRepository;

@Service
public class TvSeasonService
    extends AbstractCrudService<TvSeason, String, TvSeasonRepository> {
  protected TvSeasonService(TvSeasonRepository repository) {
    super(repository);
  }

  public Optional<TvSeason> findExternalById(String externalId) {
    return this.repository.findByExternalId(externalId);
  }

  public Optional<TvSeason> findByName(String name) {
    return this.repository.findByName(name);
  }

  public Optional<TvSeason> findByTvSeriesAndSeason(String seriesId, Integer season) {
    return this.repository.findByTvSeriesIdAndSeason(seriesId, season);
  }

}
