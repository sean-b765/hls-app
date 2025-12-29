package dev.amethyst.app.media.service;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.amethyst.app.lib.model.FileNameMetadata;
import dev.amethyst.app.lib.service.FileNameParser;
import dev.amethyst.app.media.broker.MediaMetadataBrokerService;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.media.service.MediaInfoPipelineService.MediaInfoPipelineResult.MediaInfoPipelineResultBuilder;
import dev.amethyst.app.tv.model.TvSeason;
import dev.amethyst.app.tv.model.TvSeries;
import dev.amethyst.app.tv.service.TvSeasonService;
import dev.amethyst.app.tv.service.TvSeriesService;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.tv.episode.TvEpisodeDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import lombok.Builder;
import lombok.Data;

/**
 * Contains the pipeline for creating the:
 * 1. MediaInfo based on file name
 * 2. MediaInfo based on a Movie
 * 3. MediaInfo based on a Tv Series
 * 4. MediaInfo based on a Tv Season
 * 5. MediaInfo based on a Tv Episode
 */
@Service
public class MediaInfoPipelineService {
  @Autowired
  private FileNameParser fileNameParser;
  @Autowired
  private MediaMetadataBrokerService brokerService;
  @Autowired
  private TvSeriesService tvSeriesService;
  @Autowired
  private TvSeasonService tvSeasonService;

  @Data
  @Builder
  public static class MediaInfoPipelineResult {
    private MediaInfo mediaInfo;
    private TvSeries series;
    private TvSeason season;
  }

  /**
   * <p>
   * If the Media is a movie, creates the MediaInfo directly
   * </p>
   * <p>
   * If the Media is a tv show (contains s01e01 in file name), create or lookup:
   * </p>
   * <ol>
   * <li>TvSeries</li>
   * <li>TvSeason</li>
   * <li>MediaInfo</li>
   * </ol>
   * 
   * <p>
   * This will create a new TvSeries and TvSeason,
   * ONLY if they do not already exist. Otherwise the existing one is used
   * </p>
   * 
   * <p>
   * In this case, Media and Library will not be null
   * </p>
   * 
   * @param media
   * @return
   */
  public MediaInfoPipelineResult startPipeline(Media media) {
    if (media == null || media.getLibrary() == null)
      throw new IllegalArgumentException();

    String fileName = this.fileNameParser.getFileName(media.getPath());
    FileNameMetadata fileNameMetadata = fileNameParser.parse(fileName);

    LocalDate releaseDate = fileNameMetadata.getYear() == null ? null
        : LocalDate.of(fileNameMetadata.getYear(), 1, 1);

    MediaInfo mediaInfo = MediaInfo.builder()
        .name(fileNameMetadata.getTitle())
        .media(media)
        .type(fileNameMetadata.getMediaType())
        .season(fileNameMetadata.getSeason())
        .episode(fileNameMetadata.getEpisode())
        .releaseDate(releaseDate)
        .build();

    MediaInfoPipelineResultBuilder result = MediaInfoPipelineResult.builder()
        .mediaInfo(mediaInfo);

    switch (mediaInfo.getType()) {
      case MOVIE:
        this.handleMovie(mediaInfo);
        break;
      case TV:
        this.handleTvSeries(mediaInfo, result);
      case MUSIC:
        break;
    }

    return result.build();
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  public void handleMovie(MediaInfo mediaInfo) {
    String year = mediaInfo.getReleaseDate() == null
        ? null
        : String.valueOf(Objects.requireNonNull(mediaInfo.getReleaseDate()).getYear());
    Movie brokerMovie = this.brokerService.getTmdbMovie(mediaInfo.getName(), year);
    if (brokerMovie == null)
      return;

    mediaInfo.setName(brokerMovie.getTitle());
    mediaInfo.setTagline(brokerMovie.getNewItems().getOrDefault("tagline", "").toString());
    mediaInfo.setDescription(brokerMovie.getOverview());
    mediaInfo.setBanner(brokerMovie.getBackdropPath());
    mediaInfo.setThumbnail(brokerMovie.getPosterPath());
    mediaInfo.setReleaseDate(LocalDate.parse(brokerMovie.getReleaseDate()));
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvSeries(MediaInfo mediaInfo, MediaInfoPipelineResultBuilder resultBuilder) {
    // Look for existing series
    TvSeries series = this.tvSeriesService.findByName(mediaInfo.getName()).orElse(null);

    if (series == null) {
      // Fetch TvSeries from broker
      Integer year = mediaInfo.getReleaseDate() == null
          ? null
          : Objects.requireNonNull(mediaInfo.getReleaseDate()).getYear();

      info.movito.themoviedbapi.model.core.TvSeries brokerTvSeries = this.brokerService.getTmdbTvSeries(
          mediaInfo.getName(),
          year);

      if (brokerTvSeries == null || brokerTvSeries.getName() == null)
        return;

      LocalDate releaseDate = LocalDate.parse(brokerTvSeries.getFirstAirDate());
      String externalId = String.valueOf(brokerTvSeries.getId());
      series = this.tvSeriesService.findByExternalId(externalId).orElse(null);

      mediaInfo.setName(brokerTvSeries.getName());
      mediaInfo.setDescription(brokerTvSeries.getOverview());
      mediaInfo.setBanner(brokerTvSeries.getBackdropPath());
      mediaInfo.setThumbnail(brokerTvSeries.getPosterPath());
      mediaInfo.setReleaseDate(releaseDate);

      if (series == null) {
        // New tv series
        series = TvSeries.builder()
            .name(brokerTvSeries.getName())
            .tagline(brokerTvSeries.getNewItems().getOrDefault("tagline", "").toString())
            .library(mediaInfo.getMedia().getLibrary())
            .description(brokerTvSeries.getOverview())
            .externalId(externalId)
            .banner(brokerTvSeries.getBackdropPath())
            .thumbnail(brokerTvSeries.getPosterPath())
            .releaseDate(releaseDate)
            .build();
      }
    }

    resultBuilder = resultBuilder.series(series);

    this.handleTvSeason(mediaInfo, resultBuilder);
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvSeason(MediaInfo mediaInfo, MediaInfoPipelineResultBuilder resultBuilder) {
    TvSeries series = resultBuilder.series;

    // Lookup TvSeason from broker
    TvSeasonDb brokerTvSeason = this.brokerService.getTmdbTvSeason(
        series.getExternalId(),
        mediaInfo.getSeason());

    String externalId = String.valueOf(brokerTvSeason.getId());
    // Then look if the externalId exists already before saving
    TvSeason season = this.tvSeasonService.findExternalById(externalId).orElse(null);

    if (season == null) {
      String thumbnailPath = brokerTvSeason.getImages().getPosters().getFirst().getFilePath();
      LocalDate releaseDate = LocalDate.parse(brokerTvSeason.getAirDate());

      mediaInfo.setDescription(brokerTvSeason.getOverview());
      mediaInfo.setThumbnail(thumbnailPath);
      mediaInfo.setReleaseDate(releaseDate);

      season = TvSeason.builder()
          .name(brokerTvSeason.getName())
          .description(brokerTvSeason.getOverview())
          .season(brokerTvSeason.getSeasonNumber())
          .releaseDate(releaseDate)
          .externalId(externalId)
          .banner(thumbnailPath)
          .thumbnail(thumbnailPath)
          .build();
    }

    resultBuilder = resultBuilder.season(season);

    this.handleTvEpisode(mediaInfo, resultBuilder);
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvEpisode(MediaInfo mediaInfo, MediaInfoPipelineResultBuilder resultBuilder) {
    TvSeries series = resultBuilder.series;

    TvEpisodeDb brokerTvEpisode = this.brokerService.getTmdbTvEpisode(
        series.getExternalId(),
        mediaInfo.getSeason(),
        mediaInfo.getEpisode());

    if (brokerTvEpisode == null)
      return;

    String still = brokerTvEpisode.getImages().getStills().getFirst().getFilePath();
    LocalDate releaseDate = LocalDate.parse(brokerTvEpisode.getAirDate());
    mediaInfo.setBanner(still);
    mediaInfo.setThumbnail(series.getThumbnail());
    mediaInfo.setDescription(brokerTvEpisode.getOverview());
    mediaInfo.setName(brokerTvEpisode.getName());
    mediaInfo.setReleaseDate(releaseDate);
  }

}
