package dev.seanboaden.hls.transcode;

import java.util.concurrent.Future;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranscodeWorkerHandle {
  private Process ffmpegProcess;
  private Future<?> future;
}
