package dev.amethyst.app.metadata.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Container {
  MP4,
  MKV,
  WEBM,
  AVI,
  MPEG_TS,
  MOV,
  OGG,
  MP3,
  WAV,
  FLAC,
  AAC,
  OPUS,
  UNKNOWN;

  private static final Set<Container> VIDEO = Set.of(MP4, MKV, WEBM, AVI, MPEG_TS, MOV);
  private static final Set<Container> AUDIO = Set.of(OGG, MP3, WAV, FLAC, AAC, OPUS);

  @Override
  @JsonValue
  public String toString() {
    return this.name();
  }

  public boolean isVideo() {
    return VIDEO.contains(this);
  }

  public boolean isAudio() {
    return AUDIO.contains(this);
  }
}
