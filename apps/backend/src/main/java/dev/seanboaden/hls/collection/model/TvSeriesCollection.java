package dev.seanboaden.hls.collection.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.seanboaden.hls.collection.handler.TvSeriesCollectionEventListener;
import dev.seanboaden.hls.config.base.AbstractBaseEntity;
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
@EntityListeners(value = { TvSeriesCollectionEventListener.class })
public class TvSeriesCollection extends AbstractBaseEntity {
  @Column(unique = true)
  private String externalId;

  @Column(unique = true, nullable = false)
  @NonNull
  private String name;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;

  @OneToMany(mappedBy = "tvSeries", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  @Builder.Default
  private List<TvSeasonCollection> tvSeasons = new ArrayList<>();
}
