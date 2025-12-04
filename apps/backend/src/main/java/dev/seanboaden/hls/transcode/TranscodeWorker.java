package dev.seanboaden.hls.transcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import dev.seanboaden.hls.lib.FfmpegService;
import dev.seanboaden.hls.lib.FileSystemService;
import dev.seanboaden.hls.transcode.TranscodeJob.JobType;

public class TranscodeWorker implements Runnable {
  private final FfmpegService ffmpegService;
  private final TranscodingService transcodingService;
  private final FileSystemService fileSystemService;
  private final TranscodeJob transcodeJob;
  private final String outputPath;
  private final CompletableFuture<Void> firstSegmentReadyFuture;
  private final CompletableFuture<Void> allSegmentsReadyFuture;

  public TranscodeWorker(
      FfmpegService ffmpegService,
      TranscodingService encodingService,
      FileSystemService fileSystemService,
      TranscodeJob transcodeJob,
      String outputDirectory,
      CompletableFuture<Void> firstSegmentReadyFuture,
      CompletableFuture<Void> allSegmentsReadyFuture) {
    this.ffmpegService = ffmpegService;
    this.transcodingService = encodingService;
    this.fileSystemService = fileSystemService;
    this.transcodeJob = transcodeJob;
    this.outputPath = outputDirectory;
    this.firstSegmentReadyFuture = firstSegmentReadyFuture;
    this.allSegmentsReadyFuture = allSegmentsReadyFuture;
  }

  @Override
  public void run() {
    Path outputDirectory = Path.of(this.outputPath);
    try {
      Files.createDirectories(outputDirectory);

      String[] arguments = JobType.HLS.equals(this.transcodeJob.getType())
          ? this.transcodingService.getHlsArgs(this.transcodeJob, this.outputPath)
          : new String[] {};

      Process ffmpegProcess = this.ffmpegService.ffmpeg(this.outputPath, arguments);
      if (ffmpegProcess == null) {
        this.firstSegmentReadyFuture
            .completeExceptionally(new IllegalStateException("ffmpeg process failed to create"));
        return;
      }

      Path firstSegment = outputDirectory.resolve(this.transcodeJob.getFromSegmentName());
      while (!Files.exists(firstSegment)) {
        if (Thread.currentThread().isInterrupted()) {
          ffmpegProcess.destroyForcibly();
          return;
        }
        Thread.sleep(50);
      }

      // The first segment has been completed
      this.firstSegmentReadyFuture.complete(null);

      ffmpegProcess.waitFor();
      this.allSegmentsReadyFuture.complete(null);
    } catch (IOException exception) {
      this.firstSegmentReadyFuture.completeExceptionally(exception);
      this.allSegmentsReadyFuture.completeExceptionally(exception);
    } catch (InterruptedException exception) {
      this.firstSegmentReadyFuture.completeExceptionally(exception);
      this.allSegmentsReadyFuture.completeExceptionally(exception);
    }
  }

}
