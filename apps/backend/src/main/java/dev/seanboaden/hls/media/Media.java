package dev.seanboaden.hls.media;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
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
}
