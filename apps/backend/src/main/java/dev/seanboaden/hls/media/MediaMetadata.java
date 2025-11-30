package dev.seanboaden.hls.media;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media_metadata")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaMetadata {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String fileExtension;
  private String videoCodec;
  private String audioCodec;
  private long sizeBytes;
  private double durationSeconds;
  private double framerate;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastScanDateTime;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastModified;

  @OneToOne
  @JoinColumn(name = "mediaId", nullable = false, unique = true)
  @JsonBackReference
  private Media media;
}
