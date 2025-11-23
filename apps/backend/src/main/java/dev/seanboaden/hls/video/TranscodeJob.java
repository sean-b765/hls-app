package dev.seanboaden.hls.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
@Builder
@AllArgsConstructor
public class TranscodeJob {
  private final String roomId;
  private final String inputFilePath;
  private final long timestampMs;
  private final JobType type;
  private final CompletableFuture<VideoChunk> future = new CompletableFuture<>();

  public enum JobType {
    HLS,
    MPEG_DASH
  }
}
