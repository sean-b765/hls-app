package dev.amethyst.app.media.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import dev.amethyst.app.media.handler.MediaMetadataEventListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
  private LocalDateTime lastScanDateTime;
  private LocalDateTime lastModified;

  @OneToOne(mappedBy = "metadata")
  @JsonBackReference
  private Media media;
}
