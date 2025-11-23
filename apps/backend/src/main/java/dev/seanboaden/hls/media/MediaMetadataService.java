package dev.seanboaden.hls.media;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.DurationExtractor;
import dev.seanboaden.hls.lib.FrameRateExtractor;

@Service
public class MediaMetadataService {
  @Autowired
  private MediaService mediaService;
  @Autowired
  private MediaMetadataRepository metadataRepository;
  @Autowired
  private DurationExtractor durationExtractor;
  @Autowired
  private FrameRateExtractor frameRateExtractor;

  public MediaMetadata save(MediaMetadata metadata) {
    return metadataRepository.save(metadata);
  }

  public List<MediaMetadata> findAll() {
    return metadataRepository.findAll();
  }

  public void getMetadata() {
    List<Media> medias = this.mediaService.findAll();
    medias.forEach(this::getMetadata);
  }

  public void getMetadata(Media media) {
    Optional<MediaMetadata> optionalMetadata = this.metadataRepository.findByMedia_Id(media.getId());
    File file = new File(media.getPath());

    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);
    double durationSeconds = durationExtractor.getDurationSeconds(media.getPath());
    double framerate = frameRateExtractor.getFrameRate(media.getPath());

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
}
