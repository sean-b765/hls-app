package dev.seanboaden.hls.media;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.MetadataExtractor;
import dev.seanboaden.hls.lib.MimeTypeService;
import dev.seanboaden.hls.lib.MetadataExtractor.FrameRateAndDuration;

@Service
public class MediaMetadataService {
  @Autowired
  private MediaService mediaService;
  @Autowired
  private MediaMetadataRepository metadataRepository;
  @Autowired
  private MetadataExtractor metadataExtractor;
  @Autowired
  private MimeTypeService mimeTypeService;

  public MediaMetadata save(MediaMetadata metadata) {
    return metadataRepository.save(metadata);
  }

  public List<MediaMetadata> findAll() {
    return metadataRepository.findAll();
  }

  private void getMusicMetadata(Media media) {
    Optional<MediaMetadata> optionalMetadata = this.metadataRepository.findByMedia_Id(media.getId());
    File file = new File(media.getPath());

    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);

    if (optionalMetadata.isPresent()) {
      // The metadata was already scanned
      return;
    }

    MediaMetadata metadata = MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .build();
    this.save(metadata);
  }

  private void getVideoMetadata(Media media) {
    Optional<MediaMetadata> optionalMetadata = this.metadataRepository.findByMedia_Id(media.getId());
    File file = new File(media.getPath());

    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);
    FrameRateAndDuration frameRateAndDuration = metadataExtractor.getFrameRateAndDuration(media.getPath());
    double framerate = frameRateAndDuration.getFramerate();
    double durationSeconds = frameRateAndDuration.getDuration();

    if (optionalMetadata.isPresent()) {
      // Check metadata before saving
      MediaMetadata existingMetadata = optionalMetadata.get();
      if (existingMetadata.getDurationSeconds() == durationSeconds
          && existingMetadata.getSizeBytes() == sizeBytes
          && existingMetadata.getFramerate() == framerate
          && existingMetadata.getLastModified().equals(lastModified)) {
        // The metadata was already scanned
        return;
      }
    }

    MediaMetadata metadata = MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .durationSeconds(durationSeconds)
        .framerate(framerate)
        .build();
    this.save(metadata);
  }

  public void getMetadata() {
    List<Media> medias = this.mediaService.findAll();
    medias.forEach(this::getMetadata);
  }

  public void getMetadata(Media media) {
    if (mimeTypeService.isMusicType(media.getPath())) {
      this.getMusicMetadata(media);
    } else if (mimeTypeService.isVideoType(media.getPath())) {
      this.getVideoMetadata(media);
    }
  }
}
