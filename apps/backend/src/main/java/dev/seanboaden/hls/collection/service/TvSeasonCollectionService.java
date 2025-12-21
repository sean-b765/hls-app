package dev.seanboaden.hls.collection.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.repository.TvSeasonCollectionRepository;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaService;
import jakarta.transaction.Transactional;

@Service
public class TvSeasonCollectionService {
  @Autowired
  private TvSeasonCollectionRepository tvSeasonCollectionRepository;
  @Autowired
  private MediaService mediaService;

  public TvSeasonCollection save(TvSeasonCollection tvSeason) {
    if (tvSeason == null)
      return null;
    return this.tvSeasonCollectionRepository.save(tvSeason);
  }

  public List<TvSeasonCollection> findAll() {
    return this.tvSeasonCollectionRepository.findAll();
  }

  public Optional<TvSeasonCollection> findById(String id) {
    return this.tvSeasonCollectionRepository.findById(id);
  }

  public Optional<TvSeasonCollection> findExternalById(String externalId) {
    return this.tvSeasonCollectionRepository.findByExternalId(externalId);
  }

  public Optional<TvSeasonCollection> findByName(String name) {
    return this.tvSeasonCollectionRepository.findByName(name);
  }

  public Optional<TvSeasonCollection> findByTvSeriesAndSeason(String seriesId, Integer season) {
    return this.tvSeasonCollectionRepository.findByTvSeriesIdAndSeason(seriesId, season);
  }

  @Transactional
  public void addMediaToTvSeason(String mediaId, String tvSeasonId) {
    if (tvSeasonId == null || mediaId == null)
      return;

    TvSeasonCollection tvSeason = this.tvSeasonCollectionRepository.findById(tvSeasonId)
        .orElseThrow();
    Media media = this.mediaService.findById(mediaId)
        .orElseThrow();

    // Establish links

    tvSeason.getMediaItems().add(media);
    media.setTvSeason(tvSeason);
    this.mediaService.save(media);
  }
}
