package dev.seanboaden.hls.configuration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuration")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
  @Id
  @Builder.Default
  private Long id = 1L;
  @Builder.Default
  private boolean adminUserCreated = false;
  private String mediaDirectory;
  private String transcodeDirectory;
  private String tmdbApiKey;
}
