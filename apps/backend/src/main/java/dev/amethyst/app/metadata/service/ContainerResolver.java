package dev.amethyst.app.metadata.service;

import java.util.Map;

import dev.amethyst.app.metadata.model.Container;

public class ContainerResolver {
  private static final Map<String, Container> FORMAT_MAP = Map.ofEntries(
      // MP4 family
      Map.entry("mp4", Container.MP4),
      Map.entry("m4a", Container.MP4),
      Map.entry("m4v", Container.MP4),
      Map.entry("mov", Container.MOV),
      Map.entry("mov,mp4,m4a,3gp,3g2,mj2", Container.MP4),

      // Matroska / WebM
      Map.entry("matroska", Container.MKV),
      Map.entry("webm", Container.WEBM),
      Map.entry("matroska,webm", Container.MKV),

      // MPEG transport / program streams
      Map.entry("mpegts", Container.MPEG_TS),
      Map.entry("ts", Container.MPEG_TS),

      // Legacy containers
      Map.entry("avi", Container.AVI),

      // Ogg family
      Map.entry("ogg", Container.OGG),
      Map.entry("oga", Container.OGG),
      Map.entry("ogv", Container.OGG),
      Map.entry("opus", Container.OPUS),

      // Audio containers
      Map.entry("mp3", Container.MP3),
      Map.entry("wav", Container.WAV),
      Map.entry("flac", Container.FLAC),
      Map.entry("aac", Container.AAC));

  /**
   * Resolve a normalized Container from ffprobe format_name
   */
  public static Container resolve(String formatName) {
    if (formatName == null)
      return Container.UNKNOWN;

    Container container = FORMAT_MAP.get(formatName.trim().toLowerCase());
    if (container == null)
      return Container.UNKNOWN;

    return container;
  }
}
