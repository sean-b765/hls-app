package dev.amethyst.app.media.service;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.lib.service.MimeTypeService;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.model.MediaMetadata;
import dev.amethyst.app.media.repository.MediaMetadataRepository;
import dev.amethyst.app.metadata.model.Metadata;
import dev.amethyst.app.metadata.service.MetadataExtractor;

@Service
public class MediaMetadataService extends AbstractCrudService<MediaMetadata, String, MediaMetadataRepository> {
  @Autowired
  private MetadataExtractor metadataExtractor;
  @Autowired
  private MimeTypeService mimeTypeService;

  private MediaMetadata createMusicMetadata(Media media) {
    Optional<MediaMetadata> existingMetadata = this.repository.findByMedia_Id(media.getId());
    if (existingMetadata.isPresent())
      return existingMetadata.get();

    File file = new File(media.getPath());
    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);

    return MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .build();
  }

  private MediaMetadata createVideoMetadata(Media media) {
    Optional<MediaMetadata> existingMetadata = this.repository.findByMedia_Id(media.getId());
    if (existingMetadata.isPresent())
      return existingMetadata.get();

    File file = new File(media.getPath());

    long sizeBytes = file.length();
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.UTC);
    Metadata metadata = this.metadataExtractor.getMetadata(media.getPath());

    if (metadata == null || metadata.getVideo() == null || metadata.getAudio() == null) {
      // We will attempt to retry each minute in ScheduledMediaMetadataService if null
      return null;
    }

    return MediaMetadata.builder()
        .sizeBytes(sizeBytes)
        .media(media)
        .lastScanDateTime(LocalDateTime.now())
        .lastModified(lastModified)
        .durationSeconds(metadata.getFormat().getDurationSeconds())
        .framerate(metadata.getFramerate())
        .container(metadata.getContainer())
        .bitrate(metadata.getBitrate())
        // Video
        .videoCodec(metadata.getVideoCodec())
        .videoBitDepth(metadata.getVideoBitDepth())
        .videoLevel(metadata.getVideoLevel())
        .videoWidth(metadata.getVideo().getWidth())
        .videoHeight(metadata.getVideo().getHeight())
        // Audio
        .audioCodec(metadata.getAudioCodec())
        .audioSamplerate(metadata.getAudioSamplerate())
        .audioChannelLayout(metadata.getAudio().getChannelLayout())
        .audioChannels(metadata.getAudio().getChannels())
        .build();
  }

  public MediaMetadata createMetadata(Media media) {
    if (this.mimeTypeService.isMusicType(media.getPath())) {
      return this.createMusicMetadata(media);
    } else if (this.mimeTypeService.isVideoType(media.getPath())) {
      return this.createVideoMetadata(media);
    }
    return null;
  }
}
