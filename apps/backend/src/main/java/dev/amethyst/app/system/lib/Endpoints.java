package dev.amethyst.app.system.lib;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Endpoints {
  LIBRARY("/api/library"),
  MEDIA("/api/media"),
  TV_SERIES("/api/series"),
  TV_SEASONS("/api/seasons"),
  USER("/api/user");

  private final String endpoint;

  Endpoints(String endpoint) {
    this.endpoint = endpoint;
  }

  @JsonValue
  public String getEndpoint() {
    return this.endpoint;
  }
}
