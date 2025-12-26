package dev.seanboaden.hls.media.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.seanboaden.hls.collection.model.TvSeasonCollection;
import dev.seanboaden.hls.config.base.AbstractBaseEntity;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.media.handler.MediaEventListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "media")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaEventListener.class })
public class Media extends AbstractBaseEntity {
  /**
   * The absolute path to the media file
   */
  @Column(nullable = false, unique = true)
  private String path;

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
