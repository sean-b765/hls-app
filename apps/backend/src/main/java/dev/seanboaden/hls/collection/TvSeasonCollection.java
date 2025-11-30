package dev.seanboaden.hls.collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.seanboaden.hls.media.Media;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tv_season")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TvSeasonCollection {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
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
