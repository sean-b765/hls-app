package dev.seanboaden.hls.collection;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.seanboaden.hls.media.Media;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media_collection")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaCollection {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;

  @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  @Builder.Default
  private List<Media> mediaItems = new ArrayList<>();
}
