package dev.amethyst.app.tv.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.tv.handler.TvSeasonEventListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tv_season")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { TvSeasonEventListener.class })
public class TvSeason extends AbstractBaseEntity {
  @Column(unique = true)
  private String externalId;

  private Integer season;
  @Column(unique = false, nullable = false)
  @NonNull
  private String name;
  private String description;
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "tvSeriesId", referencedColumnName = "id")
  @JsonManagedReference
  private TvSeries tvSeries;

  @OneToMany(mappedBy = "tvSeason")
  @JsonManagedReference
  @Builder.Default
  private List<Media> mediaItems = new ArrayList<>();
}
