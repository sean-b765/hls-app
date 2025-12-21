package dev.seanboaden.hls.transcode.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.seanboaden.hls.transcode.model.TranscodeJob;
import dev.seanboaden.hls.transcode.model.TranscodeJob.JobType;

public class TranscodeWorker implements Runnable {
  private final TranscodeManager transcodeManager;
  private final TranscodeJob transcodeJob;
  private final String outputPath;

  public TranscodeWorker(
      TranscodeManager transcodeManager,
      TranscodeJob transcodeJob) {
    this.transcodeJob = transcodeJob;
    this.transcodeManager = transcodeManager;
    this.outputPath = this.transcodeManager.getFileSystemService().getSegmentDirectory(transcodeJob);
  }

  @Override
  public void run() {
    Path outputDirectory = Path.of(this.outputPath);
    try {
      Files.createDirectories(outputDirectory);

      String[] arguments = JobType.HLS.equals(this.transcodeJob.getType())
          ? this.transcodeManager.getTranscodingService().getHlsArgs(this.transcodeJob, this.outputPath)
          : new String[] {};

      Process ffmpegProcess = this.transcodeManager.getFfmpegService().ffmpeg(this.outputPath, arguments);
      if (ffmpegProcess == null) {
        return;
      }
      this.transcodeManager.registerFfmpegProcess(ffmpegProcess);
      ffmpegProcess.waitFor();
    } catch (IOException | InterruptedException ignored) {
    }
  }

}
