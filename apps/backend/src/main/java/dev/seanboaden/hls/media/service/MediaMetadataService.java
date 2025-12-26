package dev.seanboaden.hls.media.service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.config.web.AsyncModifier;
import dev.seanboaden.hls.lib.service.MetadataExtractor;
import dev.seanboaden.hls.lib.service.MimeTypeService;
import dev.seanboaden.hls.lib.service.MetadataExtractor.FrameRateAndDuration;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.model.MediaMetadata;
import dev.seanboaden.hls.media.repository.MediaMetadataRepository;

@Service
public class MediaMetadataService extends AbstractCrudService<MediaMetadata, String, MediaMetadataRepository> {
  @Autowired
  private MetadataExtractor metadataExtractor;
  @Autowired
  private MimeTypeService mimeTypeService;

  protected MediaMetadataService(MediaMetadataRepository repository) {
    super(repository);
  }

  private MediaMetadata createMusicMetadata(Media media) {
    Optional<MediaMetadata> optionalMetadata = this.repository.findByMedia_Id(media.getId());
    if (optionalMetadata.isPresent())
      return null;

    File file = new File(media.getPath());
    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);

    MediaMetadata metadata = MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .build();
    return this.save(metadata);
  }

  private MediaMetadata createVideoMetadata(Media media) {
    Optional<MediaMetadata> optionalMetadata = this.repository.findByMedia_Id(media.getId());
    if (optionalMetadata.isPresent())
      return null;

    File file = new File(media.getPath());

    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);
    FrameRateAndDuration frameRateAndDuration = metadataExtractor.getFrameRateAndDuration(media.getPath());
    double framerate = frameRateAndDuration.getFramerate();
    double durationSeconds = frameRateAndDuration.getDuration();

    MediaMetadata metadata = MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .durationSeconds(durationSeconds)
        .framerate(framerate)
        .build();
    return this.save(metadata);
  }

  public MediaMetadata createMetadata(Media media) {
    if (this.mimeTypeService.isMusicType(media.getPath())) {
      return this.createMusicMetadata(media);
    } else if (this.mimeTypeService.isVideoType(media.getPath())) {
      return this.createVideoMetadata(media);
    }
    return null;
  }

  @Async(AsyncModifier.Modifier.SQLITE)
  public void ensureMetadata(Media media) {
    this.createMetadata(media);
  }
}
