package dev.seanboaden.hls.media.model;

import java.time.LocalDate;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.seanboaden.hls.config.base.AbstractBaseEntity;
import dev.seanboaden.hls.media.handler.MediaInfoEventListener;
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
@Table(name = "media_info")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaInfoEventListener.class })
public class MediaInfo extends AbstractBaseEntity {
  private String name;
  private String description;
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;
  private Integer season;
  private Integer episode;

  @OneToOne
  @JoinColumn(name = "mediaId", nullable = false, unique = true)
  @JsonBackReference
  private Media media;
}
