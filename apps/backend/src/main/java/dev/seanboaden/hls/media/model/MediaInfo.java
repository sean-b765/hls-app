package dev.seanboaden.hls.media.model;

import java.time.LocalDate;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.seanboaden.hls.media.handler.MediaInfoEventListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "media_info")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = { MediaInfoEventListener.class })
public class MediaInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Nullable
  private LocalDate releaseDate;
  private String thumbnail;
  private String banner;
  private Integer season;
  private Integer episode;

  @OneToOne
  @JoinColumn(name = "mediaId", nullable = false, unique = true)
  @JsonBackReference
  private Media media;
}
