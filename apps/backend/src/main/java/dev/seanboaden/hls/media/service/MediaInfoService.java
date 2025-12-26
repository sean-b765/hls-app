package dev.seanboaden.hls.media.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.collection.service.TvSeasonCollectionService;
import dev.seanboaden.hls.collection.service.TvSeriesCollectionService;
import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.config.web.AsyncModifier;
import dev.seanboaden.hls.lib.model.FileNameMetadata;
import dev.seanboaden.hls.lib.service.FileNameParser;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.media.repository.MediaInfoRepository;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.core.TvSeriesResultsPage;
import info.movito.themoviedbapi.model.tv.episode.TvEpisodeDb;
import info.movito.themoviedbapi.model.tv.season.TvSeasonDb;
import info.movito.themoviedbapi.tools.TmdbException;
import info.movito.themoviedbapi.tools.appendtoresponse.TvEpisodesAppendToResponse;
import info.movito.themoviedbapi.tools.appendtoresponse.TvSeasonsAppendToResponse;

@Service
public class MediaInfoService extends AbstractCrudService<MediaInfo, String, MediaInfoRepository> {
  @Autowired
  private TvSeriesCollectionService tvSeriesService;
  @Autowired
  private TvSeasonCollectionService tvSeasonService;
  @Autowired
  private FileNameParser fileNameParser;
  @Autowired
  private TmdbApi tmdbApi;

  protected MediaInfoService(MediaInfoRepository repository) {
    super(repository);
  }

  private Movie getTmdbMovie(MediaInfo mediaInfo) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      String year = null;
      if (mediaInfo.getReleaseDate() != null) {
        year = String.valueOf(Objects.requireNonNull(mediaInfo.getReleaseDate()).getYear());
      }
      MovieResultsPage movieResultsPage = tmdbSearch.searchMovie(mediaInfo.getName(), true, null, year, 0, null, null);
      Optional<Movie> movie = movieResultsPage.getResults().stream().findFirst();
      if (movie.isEmpty())
        return null;
      return movie.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  private TvSeries getTmdbTvSeries(MediaInfo mediaInfo) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      Integer year = null;
      if (mediaInfo.getReleaseDate() != null) {
        year = Objects.requireNonNull(mediaInfo.getReleaseDate()).getYear();
      }
      TvSeriesResultsPage tvResultsPage = tmdbSearch.searchTv(mediaInfo.getName(), year, null, null, 0, null);
      Optional<TvSeries> tv = tvResultsPage.getResults().stream().findFirst();
      if (tv.isEmpty())
        return null;
      return tv.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  private TvSeasonDb getTmdbTvSeason(String seriesId, MediaInfo mediaInfo) {
    try {
      return this.tmdbApi.getTvSeasons()
          .getDetails(
              Integer.parseInt(seriesId),
              mediaInfo.getSeason(),
              null,
              TvSeasonsAppendToResponse.IMAGES);
    } catch (NumberFormatException | TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  private TvEpisodeDb getTmdbTvEpisode(String seriesId, MediaInfo mediaInfo) {
    try {
      return this.tmdbApi.getTvEpisodes()
          .getDetails(
              Integer.parseInt(seriesId),
              mediaInfo.getSeason(),
              mediaInfo.getEpisode(),
              null,
              TvEpisodesAppendToResponse.IMAGES);
    } catch (NumberFormatException | TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvEpisode(
      MediaInfo mediaInfo,
      TvSeasonCollection tvSeasonCollection,
      TvSeriesCollection tvSeriesCollection) {
    Optional<TvEpisodeDb> tmdbTvEpisode = Optional
        .of(this.getTmdbTvEpisode(tvSeriesCollection.getExternalId(), mediaInfo));
    if (tmdbTvEpisode.isEmpty())
      return;

    TvEpisodeDb tvEpisodeDb = tmdbTvEpisode.get();
    String still = tvEpisodeDb.getImages().getStills().getFirst().getFilePath();
    LocalDate releaseDate = LocalDate.parse(tvEpisodeDb.getAirDate());
    mediaInfo.setBanner(still);
    mediaInfo.setThumbnail(tvSeriesCollection.getThumbnail());
    mediaInfo.setDescription(tvEpisodeDb.getOverview());
    mediaInfo.setName(tvEpisodeDb.getName());
    mediaInfo.setReleaseDate(releaseDate);
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvSeason(MediaInfo mediaInfo, TvSeriesCollection tvSeriesCollection, Media media) {
    TvSeasonCollection tvSeasonCollection = this.tvSeasonService
        .findByTvSeriesAndSeason(tvSeriesCollection.getId(), mediaInfo.getSeason())
        .orElse(null);
    if (tvSeasonCollection == null) {
      // Create TvSeason
      Optional<TvSeasonDb> tmdbTvSeason = Optional
          .of(this.getTmdbTvSeason(tvSeriesCollection.getExternalId(), mediaInfo));
      if (tmdbTvSeason.isEmpty())
        return;

      String externalId = String.valueOf(tmdbTvSeason.get().getId());
      // Then look if the externalId exists already before saving
      tvSeasonCollection = this.tvSeasonService.findExternalById(externalId).orElse(null);

      if (tvSeasonCollection == null) {
        TvSeasonDb tvSeasonDb = tmdbTvSeason.get();
        String thumbnailPath = tvSeasonDb.getImages().getPosters().getFirst().getFilePath();
        LocalDate releaseDate = LocalDate.parse(tvSeasonDb.getAirDate());

        mediaInfo.setDescription(tvSeasonDb.getOverview());
        mediaInfo.setThumbnail(thumbnailPath);
        mediaInfo.setReleaseDate(releaseDate);

        TvSeasonCollection toSave = TvSeasonCollection.builder()
            .name(tvSeasonDb.getName())
            .description(tvSeasonDb.getOverview())
            .season(tvSeasonDb.getSeasonNumber())
            .releaseDate(releaseDate)
            .externalId(String.valueOf(tvSeasonDb.getId()))
            .banner(thumbnailPath)
            .thumbnail(thumbnailPath)
            .build();
        tvSeasonCollection = this.tvSeasonService.save(toSave);
      }
    }
    // Establish links in our db
    this.tvSeriesService.addTvSeasonToTvSeries(tvSeasonCollection.getId(), tvSeriesCollection.getId());
    this.tvSeasonService.addMediaToTvSeason(media.getId(), tvSeasonCollection.getId());

    this.handleTvEpisode(mediaInfo, tvSeasonCollection, tvSeriesCollection);
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleTvSeries(MediaInfo mediaInfo, Media media) {
    String sourceName = mediaInfo.getName();
    TvSeriesCollection tvSeriesCollection = this.tvSeriesService.findByName(sourceName).orElse(null);
    if (tvSeriesCollection == null) {
      // Create TvSeries
      Optional<TvSeries> tmdbTvSeries = Optional.of(this.getTmdbTvSeries(mediaInfo));
      if (tmdbTvSeries.isEmpty())
        return;

      TvSeries tvSeries = tmdbTvSeries.get();
      if (tvSeries == null || tvSeries.getName() == null)
        return;

      String realName = tvSeries.getName();
      LocalDate releaseDate = LocalDate.parse(tvSeries.getFirstAirDate());

      mediaInfo.setName(realName);
      mediaInfo.setDescription(tvSeries.getOverview());
      mediaInfo.setBanner(tvSeries.getBackdropPath());
      mediaInfo.setThumbnail(tvSeries.getPosterPath());
      mediaInfo.setReleaseDate(releaseDate);

      boolean isNameAccurate = realName.equals(sourceName);
      if (!isNameAccurate) {
        // The name wasn't exactly accurate from our FileNameParser...
        // Look for it again by before saving a potentially duplicate TvSeriesCollection
        tvSeriesCollection = this.tvSeriesService.findByName(realName).orElse(null);
      }

      if (tvSeriesCollection == null) {
        // Save a new tv series
        TvSeriesCollection toSave = TvSeriesCollection.builder()
            .name(realName)
            .description(tvSeries.getOverview())
            .externalId(String.valueOf(tvSeries.getId()))
            .banner(tvSeries.getBackdropPath())
            .thumbnail(tvSeries.getPosterPath())
            .releaseDate(releaseDate)
            .build();
        tvSeriesCollection = this.tvSeriesService.save(toSave);
      }
    }

    this.handleTvSeason(mediaInfo, tvSeriesCollection, media);
  }

  /**
   * this method may mutate mediaInfo
   * 
   * @param mediaInfo
   */
  private void handleMovie(MediaInfo mediaInfo) {
    Movie movie = this.getTmdbMovie(mediaInfo);
    if (movie == null)
      return;

    mediaInfo.setDescription(movie.getOverview());
    mediaInfo.setBanner(movie.getBackdropPath());
    mediaInfo.setThumbnail(movie.getPosterPath());
    mediaInfo.setReleaseDate(LocalDate.parse(movie.getReleaseDate()));
  }

  /**
   * <p>
   * If the Media is a movie, creates the MediaInfo directly
   * </p>
   * <p>
   * If the Media is a tv show (contains s01e01 in file name), create:
   * </p>
   * <ol>
   * <li>TvSeriesCollection</li>
   * <li>TvSeasonCollection</li>
   * <li>MediaInfo</li>
   * </ol>
   * 
   * This will create the TvSeriesCollection and TvSeasonCollection,
   * ONLY if they do not already exist
   * 
   * @param media
   * @return
   */
  public MediaInfo createMediaInfo(Media media) {
    // Already exists, meaning we've already performed the operations below
    Optional<MediaInfo> existingOptional = this.repository.findByMediaId(media.getId());
    if (existingOptional.isPresent())
      return existingOptional.get();

    String fileName = this.fileNameParser.getFileName(media.getPath());
    FileNameMetadata fileNameMetadata = fileNameParser.parse(fileName);

    LocalDate releaseDate = fileNameMetadata.getYear() == null ? null
        : LocalDate.of(fileNameMetadata.getYear(), 1, 1);
    MediaInfo mediaInfo = MediaInfo.builder()
        .media(media)
        .name(fileNameMetadata.getTitle())
        .season(fileNameMetadata.getSeason())
        .episode(fileNameMetadata.getEpisode())
        .releaseDate(releaseDate)
        .build();

    if (mediaInfo.getSeason() != null) {
      this.handleTvSeries(mediaInfo, media);
    } else {
      this.handleMovie(mediaInfo);
    }
    return this.save(mediaInfo);
  }

  @Async(AsyncModifier.Modifier.SQLITE)
  public void ensureMediaInfo(Media media) {
    this.createMediaInfo(media);
  }
}
