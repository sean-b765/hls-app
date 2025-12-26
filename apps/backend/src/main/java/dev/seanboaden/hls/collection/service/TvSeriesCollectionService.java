package dev.seanboaden.hls.collection.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.collection.repository.TvSeriesCollectionRepository;
import dev.seanboaden.hls.config.base.AbstractCrudService;
import jakarta.transaction.Transactional;

@Service
public class TvSeriesCollectionService
    extends AbstractCrudService<TvSeriesCollection, String, TvSeriesCollectionRepository> {
  @Autowired
  private TvSeasonCollectionService tvSeasonCollectionService;

  protected TvSeriesCollectionService(TvSeriesCollectionRepository repository) {
    super(repository);
  }

  public Optional<TvSeriesCollection> findByName(String name) {
    return this.repository.findByName(name);
  }

  @Transactional
  public void addTvSeasonToTvSeries(String tvSeasonId, String tvSeriesId) {
    if (tvSeriesId == null || tvSeasonId == null)
      return;

    TvSeriesCollection tvSeries = this.repository
        .findById(tvSeriesId)
        .orElseThrow();

    TvSeasonCollection tvSeason = this.tvSeasonCollectionService.findById(tvSeasonId)
        .orElseThrow();

    // Estalish links

    // TvSeries -> TvSeasons
    tvSeries.getTvSeasons().add(tvSeason);
    // TvSeason -> TvSerie
    tvSeason.setTvSeries(tvSeries);
    this.tvSeasonCollectionService.save(tvSeason);
  }
}
