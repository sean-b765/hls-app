package dev.seanboaden.hls.transcode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranscodeWorkerHandle {
  private Process ffmpegProcess;
  private Future<?> future;
  private CompletableFuture<Void> firstSegmentReadyFuture;
  private CompletableFuture<Void> allSegmentsReadyFuture;
}
