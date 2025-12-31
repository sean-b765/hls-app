package dev.amethyst.app.tv.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.tv.model.TvSeason;
import dev.amethyst.app.tv.repository.TvSeasonRepository;

@Service
public class TvSeasonService
    extends AbstractCrudService<TvSeason, String, TvSeasonRepository> {
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
