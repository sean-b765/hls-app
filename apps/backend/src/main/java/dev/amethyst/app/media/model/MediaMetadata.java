package dev.amethyst.app.media.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import dev.amethyst.app.media.handler.MediaMetadataEventListener;
import dev.amethyst.app.metadata.model.Container;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  private String videoCodec;
  private Integer videoBitDepth;
  private Integer videoLevel;
  private Integer videoWidth;
  private Integer videoHeight;

  private String audioCodec;
  private Integer audioSamplerate;
  private Integer audioChannels;
  private String audioChannelLayout;

  private Long bitrate;
  @Enumerated(EnumType.STRING)
  private Container container;
  private Long sizeBytes;
  private Double durationSeconds;
  private Long framerate;
  private LocalDateTime lastScanDateTime;
  private LocalDateTime lastModified;

  @OneToOne(mappedBy = "metadata")
  @JsonBackReference
  private Media media;
}
