package dev.seanboaden.hls.collection.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.seanboaden.hls.collection.handler.TvSeasonCollectionEventListener;
import dev.seanboaden.hls.config.base.AbstractBaseEntity;
import dev.seanboaden.hls.media.model.Media;
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
@EntityListeners(value = { TvSeasonCollectionEventListener.class })
public class TvSeasonCollection extends AbstractBaseEntity {
  @Column(unique = true)
  private String externalId;

  private Integer season;
  @Column(unique = false, nullable = false)
  private String name;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;

  @ManyToOne
  @JoinColumn(name = "tvSeriesId", referencedColumnName = "id")
  private TvSeriesCollection tvSeries;

  @OneToMany(mappedBy = "tvSeason", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  @Builder.Default
  private List<Media> mediaItems = new ArrayList<>();
}
