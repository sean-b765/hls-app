package dev.seanboaden.hls.media.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MediaType {
  MOVIE("MOVIE"),
  TV("TV"),
  MUSIC("MUSIC");

  private final String value;

  MediaType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return this.value;
  }
}
