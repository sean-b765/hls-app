package dev.amethyst.app.media.broker;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.amethyst.app.media.model.MediaInfo;
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
public class MediaMetadataBrokerService {
  @Autowired
  private TmdbApi tmdbApi;

  public Movie getTmdbMovie(String title, String year) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      MovieResultsPage movieResultsPage = tmdbSearch.searchMovie(title, true, null, year, 0, null, null);
      Optional<Movie> movie = movieResultsPage.getResults().stream().findFirst();
      if (movie.isEmpty())
        return null;
      return movie.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  public TvSeries getTmdbTvSeries(String title, Integer year) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      TvSeriesResultsPage tvResultsPage = tmdbSearch.searchTv(title, year, null, null, 0, null);
      Optional<TvSeries> tv = tvResultsPage.getResults().stream().findFirst();
      if (tv.isEmpty())
        return null;
      return tv.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  public TvSeasonDb getTmdbTvSeason(String seriesId, Integer season) {
    try {
      return this.tmdbApi.getTvSeasons()
          .getDetails(
              Integer.parseInt(seriesId),
              season,
              null,
              TvSeasonsAppendToResponse.IMAGES);
    } catch (NumberFormatException | TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  public TvEpisodeDb getTmdbTvEpisode(String seriesId, Integer season, Integer episode) {
    try {
      return this.tmdbApi.getTvEpisodes()
          .getDetails(
              Integer.parseInt(seriesId),
              season,
              episode,
              null,
              TvEpisodesAppendToResponse.IMAGES);
    } catch (NumberFormatException | TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }
}
