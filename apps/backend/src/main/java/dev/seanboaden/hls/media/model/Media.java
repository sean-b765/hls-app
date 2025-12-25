package dev.seanboaden.hls.media.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.media.handler.MediaEventListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaEventListener.class })
public class Media {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  /**
   * The absolute path to the media file
   */
  @Column(nullable = false, unique = true)
  private String path;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "libraryId", referencedColumnName = "id")
  @JsonManagedReference
  private Library library;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "media")
  @JoinColumn(name = "metadataId", referencedColumnName = "id")
  @JsonManagedReference
  private MediaMetadata metadata;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "media")
  @JoinColumn(name = "mediaInfoId", referencedColumnName = "id")
  @JsonManagedReference
  private MediaInfo info;

  @ManyToOne
  @JoinColumn(name = "tvSeasonId", referencedColumnName = "id")
  @JsonManagedReference
  private TvSeasonCollection tvSeason;
}
