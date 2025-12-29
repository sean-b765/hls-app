package dev.amethyst.app.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.config.web.AsyncModifier;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.model.MediaInfo;
import dev.amethyst.app.media.model.MediaMetadata;
import dev.amethyst.app.media.model.MediaType;
import dev.amethyst.app.media.repository.MediaRepository;
import dev.amethyst.app.media.service.MediaInfoPipelineService.MediaInfoPipelineResult;
import dev.amethyst.app.tv.model.TvSeason;
import dev.amethyst.app.tv.model.TvSeries;
import dev.amethyst.app.tv.service.TvSeasonService;
import dev.amethyst.app.tv.service.TvSeriesService;
import jakarta.transaction.Transactional;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MediaService extends AbstractCrudService<Media, String, MediaRepository> {
  @Autowired
  private MediaMetadataService mediaMetadataService;
  @Autowired
  private MediaInfoPipelineService mediaInfoPipelineService;
  @Autowired
  private TvSeriesService tvSeriesService;
  @Autowired
  private TvSeasonService tvSeasonService;

  protected MediaService(MediaRepository repository) {
    super(repository);
  }

  public Optional<Media> findByPath(String absolutePath) {
    return this.repository.findByPath(absolutePath);
  }

  public List<Media> findAllWhereTvSeasonIsNull() {
    return this.repository.findByTvSeasonIsNull();
  }

  public List<Media> findAllWhereMetadataIsNullOrInfoIsNull() {
    return this.repository.findByMetadataIsNullOrInfoIsNull();
  }

  public boolean existsByPath(String path) {
    return this.findByPath(path).isPresent();
  }

  public Media buildFromFile(File file) {
    return Media.builder().path(file.getAbsolutePath()).build();
  }

  public void deleteAllByLibraryId(String libraryId) {
    this.repository.deleteAllByLibrary_Id(libraryId);
  }

  @Async(AsyncModifier.Modifier.SQLITE)
  public void ensureMetadata(String mediaId) {
    if (mediaId == null)
      return;

    Media media = this.findById(mediaId).orElseThrow();

    MediaMetadata mediaMetadata = this.mediaMetadataService.createMetadata(media);
    mediaMetadata.setMedia(media);
    media.setMetadata(mediaMetadata);
    this.save(media);
  }

  @Async(AsyncModifier.Modifier.SQLITE)
  public void ensureInfo(String mediaId) {
    if (mediaId == null)
      return;

    Media media = this.findById(mediaId).orElseThrow();

    MediaInfoPipelineResult result = this.mediaInfoPipelineService.startPipeline(media);
    this.ensureInfo(media.getId(), result);
  }

  @Transactional
  private void ensureInfo(String mediaId, MediaInfoPipelineResult result) {
    if (mediaId == null)
      throw new NoSuchElementException();

    Media media = this.findById(mediaId).orElseThrow();

    MediaInfo info = result.getMediaInfo();
    TvSeries series = result.getSeries();
    TvSeason season = result.getSeason();
    if (info == null)
      return;

    info.setMedia(media);
    media.setInfo(info);

    if (!info.getType().equals(MediaType.TV)) {
      this.save(media);
      return;
    }

    if (series.getId() == null) {
      series.setLibrary(media.getLibrary());
      series = this.tvSeriesService.save(series);
    }

    if (season.getId() == null) {
      season = this.tvSeasonService.save(season);
    }

    // Establish relationships
    season.setTvSeries(series);
    media.setTvSeason(season);
    this.save(media);
  }
}
