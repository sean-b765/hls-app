package dev.amethyst.app.lib.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MimeTypeService {
  public final Map<String, String> videoTypes = Map.of(
      "mkv", "video/x-matroska",
      "mp4", "video/mp4");

  public final Map<String, String> musicTypes = Map.of(
      "mp3", "audio/mp3");

  public boolean isVideoType(String fileName) {
    String extension = fileName.contains(".") ? StringUtils.substringAfterLast(fileName, ".") : fileName;
    return videoTypes.containsKey(extension);
  }

  public boolean isMusicType(String fileName) {
    String extension = fileName.contains(".") ? StringUtils.substringAfterLast(fileName, ".") : fileName;
    return musicTypes.containsKey(extension);
  }
}
