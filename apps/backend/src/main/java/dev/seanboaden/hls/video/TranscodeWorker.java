package dev.seanboaden.hls.video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import dev.seanboaden.hls.encoding.EncodingService;
import dev.seanboaden.hls.lib.FfmpegService;
import dev.seanboaden.hls.lib.FileSystemService;
import dev.seanboaden.hls.video.TranscodeJob.JobType;

public class TranscodeWorker implements Runnable {
  private final FfmpegService ffmpegService;
  private final EncodingService encodingService;
  private final FileSystemService fileSystemService;
  private final TranscodeJob transcodeJob;
  private final String outputPath;
  private final CompletableFuture<Void> firstSegmentReadyFuture;

  public TranscodeWorker(
      FfmpegService ffmpegService,
      EncodingService encodingService,
      FileSystemService fileSystemService,
      TranscodeJob transcodeJob,
      String outputDirectory,
      CompletableFuture<Void> firstSegmentReadyFuture) {
    this.ffmpegService = ffmpegService;
    this.encodingService = encodingService;
    this.fileSystemService = fileSystemService;
    this.transcodeJob = transcodeJob;
    this.outputPath = outputDirectory;
    this.firstSegmentReadyFuture = firstSegmentReadyFuture;
  }

  @Override
  public void run() {
    Path outputDirectory = Path.of(this.outputPath);
    try {
      Files.createDirectories(outputDirectory);

      String[] arguments = JobType.HLS.equals(this.transcodeJob.getType())
          ? this.encodingService.getHlsArgs(this.transcodeJob, this.outputPath)
          : new String[] {};

      Process ffmpegProcess = this.ffmpegService.ffmpeg(this.outputPath, arguments);
      if (ffmpegProcess == null) {
        this.firstSegmentReadyFuture
            .completeExceptionally(new IllegalStateException("ffmpeg process failed to create"));
        return;
      }

      Path firstSegment = outputDirectory.resolve("segment00000.ts");
      System.out.println("Looking for: " + firstSegment.toString() + "\n\tRunning with args:\n\t" + arguments);
      while (!Files.exists(firstSegment)) {
        if (Thread.currentThread().isInterrupted()) {
          ffmpegProcess.destroyForcibly();
          return;
        }
        Thread.sleep(50);
      }

      System.out.println("Found! sending back by continuing transcode");

      // The first segment has been completed
      this.firstSegmentReadyFuture.complete(null);

      ffmpegProcess.waitFor();
    } catch (IOException exception) {
      this.firstSegmentReadyFuture.completeExceptionally(exception);
    } catch (InterruptedException exception) {
      this.firstSegmentReadyFuture.completeExceptionally(exception);
    }
  }

}
