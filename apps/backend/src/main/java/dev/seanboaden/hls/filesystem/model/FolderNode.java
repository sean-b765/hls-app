package dev.seanboaden.hls.filesystem.model;

import org.springframework.lang.NonNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderNode {
  @NonNull
  private String name;
  /**
   * Path relative to MEDIA_ROOT
   */
  @NonNull
  private String path;
  @NonNull
  @Builder.Default
  private Boolean hasChildren = false;
}
