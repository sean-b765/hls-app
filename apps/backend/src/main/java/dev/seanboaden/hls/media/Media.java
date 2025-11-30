package dev.seanboaden.hls.media;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.seanboaden.hls.collection.MediaCollection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Media {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  /**
   * The absolute path to the media file
   */
  @Column(nullable = false, unique = true)
  private String path;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "media")
  @JoinColumn(name = "metadataId", referencedColumnName = "id")
  @JsonManagedReference
  private MediaMetadata metadata;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "media")
  @JoinColumn(name = "mediaInfoId", referencedColumnName = "id")
  @JsonManagedReference
  private MediaInfo info;

  @ManyToOne
  @JoinColumn(name = "mediaCollectionId", referencedColumnName = "id")
  @JsonManagedReference
  private MediaCollection collection;
}
