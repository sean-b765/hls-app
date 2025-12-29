package dev.seanboaden.hls.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.collection.service.TvSeasonCollectionService;
import dev.seanboaden.hls.collection.service.TvSeriesCollectionService;
import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.config.web.AsyncModifier;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.model.MediaInfo;
import dev.seanboaden.hls.media.model.MediaMetadata;
import dev.seanboaden.hls.media.model.MediaType;
import dev.seanboaden.hls.media.repository.MediaRepository;
import dev.seanboaden.hls.media.service.MediaInfoPipelineService.MediaInfoPipelineResult;
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
  private MediaInfoService mediaInfoService;
  @Autowired
  private MediaInfoPipelineService mediaInfoPipelineService;
  @Autowired
  private TvSeriesCollectionService tvSeriesService;
  @Autowired
  private TvSeasonCollectionService tvSeasonService;

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
    TvSeriesCollection series = result.getSeries();
    TvSeasonCollection season = result.getSeason();
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
