package dev.seanboaden.hls.media;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.FileNameMetadata;
import dev.seanboaden.hls.lib.FileNameParser;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.TvSeries;
import info.movito.themoviedbapi.model.core.TvSeriesResultsPage;
import info.movito.themoviedbapi.tools.TmdbException;

@Service
public class MediaInfoService {
  @Autowired
  private MediaInfoRepository mediaInfoRepository;
  @Autowired
  private FileNameParser fileNameParser;
  @Autowired
  private TmdbApi tmdbApi;

  public MediaInfo save(MediaInfo mediaInfo) {
    return mediaInfoRepository.save(mediaInfo);
  }

  private Movie getTmdbMovie(String search) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      MovieResultsPage movieResultsPage = tmdbSearch.searchMovie(search, true, null, null, 0, null, null);
      Optional<Movie> movie = movieResultsPage.getResults().stream().findFirst();
      if (movie.isEmpty())
        return null;
      return movie.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  private TvSeries getTmdbTv(String search, int season, int episode) {
    try {
      TmdbSearch tmdbSearch = new TmdbSearch(this.tmdbApi);
      TvSeriesResultsPage tvResultsPage = tmdbSearch.searchTv(search, null, null, null, 0, null);
      Optional<TvSeries> tv = tvResultsPage.getResults().stream().findFirst();
      if (tv.isEmpty())
        return null;
      return tv.get();
    } catch (TmdbException e) {
      e.printStackTrace();
      return null;
    }
  }

  public MediaInfo getInfo(Media media) {
    Optional<MediaInfo> existingOptional = this.mediaInfoRepository.findByMedia_Id(media.getId());
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
      // tv
      TvSeries tv = this.getTmdbTv(mediaInfo.getName(), mediaInfo.getSeason(), mediaInfo.getEpisode());
      if (tv != null) {
        mediaInfo.setDescription(tv.getOverview());
        mediaInfo.setBanner(tv.getBackdropPath());
        mediaInfo.setThumbnail(tv.getPosterPath());
        mediaInfo.setReleaseDate(LocalDate.parse(tv.getFirstAirDate()));
      }
    } else {
      // movie
      Movie movie = this.getTmdbMovie(mediaInfo.getName());
      if (movie != null) {
        mediaInfo.setDescription(movie.getOverview());
        mediaInfo.setBanner(movie.getBackdropPath());
        mediaInfo.setThumbnail(movie.getPosterPath());
        mediaInfo.setReleaseDate(LocalDate.parse(movie.getReleaseDate()));
      }
    }
    return this.save(mediaInfo);
  }
}
