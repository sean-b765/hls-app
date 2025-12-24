package dev.seanboaden.hls.filesystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderNode {
  private String name;
  /**
   * Path relative to MEDIA_ROOT
   */
  private String path;
  @Builder.Default
  private boolean hasChildren = false;
}
