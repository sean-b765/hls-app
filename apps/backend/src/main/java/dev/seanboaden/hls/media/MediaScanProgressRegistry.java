package dev.seanboaden.hls.media;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MediaScanProgressRegistry {
  private final Map<String, MediaProgressEnum> progress = new ConcurrentHashMap<>();

  public void updateProgress(String mediaId, MediaProgressEnum status) {
    progress.put(mediaId, status);
  }

  public MediaProgressEnum getProgress(String mediaId) {
    return progress.get(mediaId);
  }

  public Map<String, MediaProgressEnum> getProgress() {
    return progress;
  }

  public void clear(String mediaId) {
    progress.remove(mediaId);
  }

  public void clear() {
    progress.clear();
  }
}
