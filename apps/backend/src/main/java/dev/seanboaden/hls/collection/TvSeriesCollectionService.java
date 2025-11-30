package dev.seanboaden.hls.collection;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TvSeriesCollectionService {
  @Autowired
  private TvSeriesCollectionRepository tvSeriesCollectionRepository;
  @Autowired
  private TvSeasonCollectionService tvSeasonCollectionService;

  public TvSeriesCollection save(TvSeriesCollection tvSeries) {
    if (tvSeries == null)
      return null;
    return this.tvSeriesCollectionRepository.save(tvSeries);
  }

  public List<TvSeriesCollection> findAll() {
    return this.tvSeriesCollectionRepository.findAll();
  }

  public Optional<TvSeriesCollection> findByName(String name) {
    return this.tvSeriesCollectionRepository.findByName(name);
  }

  @Transactional
  public void addTvSeasonToTvSeries(String tvSeasonId, String tvSeriesId) {
    if (tvSeriesId == null || tvSeasonId == null)
      return;

    TvSeriesCollection tvSeries = this.tvSeriesCollectionRepository
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
