package dev.seanboaden.hls.media.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.seanboaden.hls.config.base.AbstractBaseEntity;
import dev.seanboaden.hls.media.handler.MediaMetadataEventListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "media_metadata")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaMetadataEventListener.class })
public class MediaMetadata extends AbstractBaseEntity {
  private String fileExtension;
  private String videoCodec;
  private String audioCodec;
  private long sizeBytes;
  private double durationSeconds;
  private double framerate;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastScanDateTime;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastModified;

  @OneToOne
  @JoinColumn(name = "mediaId", nullable = false, unique = true)
  @JsonBackReference
  private Media media;
}
