package dev.seanboaden.hls.lib;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MimeTypeService {
  public final Map<String, String> videoTypes = Map.of(
      "mkv", "video/x-matroska",
      "mp4", "video/mp4");

  public boolean isVideoType(String fileName) {
    String extension = StringUtils.substringAfterLast(fileName, ".");
    return videoTypes.containsKey(extension);
  }
}
