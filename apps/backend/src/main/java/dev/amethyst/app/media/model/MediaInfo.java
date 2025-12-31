package dev.amethyst.app.media.model;

import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import dev.amethyst.app.media.handler.MediaInfoEventListener;
import jakarta.persistence.Column;
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
@Table(name = "media_info")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaInfoEventListener.class })
public class MediaInfo extends AbstractBaseEntity {
  @NonNull
  @Column(nullable = false)
  private String name;
  @Enumerated(EnumType.STRING)
  private MediaType type;
  private String tagline;
  private String description;
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;
  private Integer season;
  private Integer episode;

  @OneToOne(mappedBy = "info")
  @JsonBackReference
  private Media media;
}
