package dev.seanboaden.hls.tv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.tv.model.TvSeason;
import dev.seanboaden.hls.tv.model.TvSeries;
import dev.seanboaden.hls.tv.repository.TvSeriesRepository;
import jakarta.transaction.Transactional;

@Service
public class TvSeriesService
    extends AbstractCrudService<TvSeries, String, TvSeriesRepository> {
  @Autowired
  private TvSeasonService tvSeasonService;

  protected TvSeriesService(TvSeriesRepository repository) {
    super(repository);
  }

  public Optional<TvSeries> findByName(String name) {
    return this.repository.findByName(name);
  }

  public Optional<TvSeries> findByExternalId(String externalId) {
    return this.repository.findByExternalId(externalId);
  }

  @Transactional
  public void addTvSeasonToTvSeries(String tvSeasonId, String tvSeriesId) {
    if (tvSeriesId == null || tvSeasonId == null)
      return;

    TvSeries tvSeries = this.repository
        .findById(tvSeriesId)
        .orElseThrow();

    TvSeason tvSeason = this.tvSeasonService.findById(tvSeasonId)
        .orElseThrow();

    // Estalish links

    // TvSeries -> TvSeasons
    tvSeries.getTvSeasons().add(tvSeason);
    // TvSeason -> TvSerie
    tvSeason.setTvSeries(tvSeries);
    this.tvSeasonService.save(tvSeason);
  }
}
