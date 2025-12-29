package dev.amethyst.app.tv.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.amethyst.app.config.base.LibraryEntity;
import dev.amethyst.app.tv.handler.TvSeriesEventListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tv_series")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { TvSeriesEventListener.class })
public class TvSeries extends LibraryEntity {
  @Column(unique = true)
  private String externalId;

  @Column(unique = false, nullable = false)
  @NonNull
  private String name;
  private String tagline;
  private String description;
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;

  @OneToMany(mappedBy = "tvSeries")
  @JsonBackReference
  @Builder.Default
  private List<TvSeason> tvSeasons = new ArrayList<>();
}
