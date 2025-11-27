package dev.seanboaden.hls.video;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.encoding.EncodingService;
import dev.seanboaden.hls.lib.FfmpegService;
import dev.seanboaden.hls.lib.FileSystemService;

@Service
public class TranscodeManager {
  @Autowired
  private FfmpegService ffmpegService;
  @Autowired
  private EncodingService encodingService;
  @Autowired
  private FileSystemService fileSystemService;

  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final ConcurrentHashMap<String, TranscodeWorkerHandle> workers = new ConcurrentHashMap<>();

  private String getWorkerKey(TranscodeJob transcodeJob) {
    return StringUtils.joinWith(
        ":",
        transcodeJob.getRoomCode(),
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName(),
        transcodeJob.getType().name());
  }

  public CompletableFuture<Void> startOrRetrieveWorker(TranscodeJob transcodeJob) {
    String workerKey = this.getWorkerKey(transcodeJob);

    return workers
        .compute(workerKey, (key, worker) -> {
          if (worker != null && !worker.getFuture().isDone()) {
            // Already working
            return worker;
          }

          // New worker is needed
          CompletableFuture<Void> firstSegmentReadyFuture = new CompletableFuture<>();
          String outputPath = this.fileSystemService.getSegmentDirectory(transcodeJob);

          TranscodeWorker workerThread = new TranscodeWorker(
              ffmpegService,
              encodingService,
              fileSystemService,
              transcodeJob,
              outputPath,
              firstSegmentReadyFuture);
          Future<?> future = executorService.submit(workerThread);

          return TranscodeWorkerHandle.builder()
              .firstSegmentReadyFuture(firstSegmentReadyFuture)
              .future(future)
              .build();
        })
        .getFirstSegmentReadyFuture();
  }

  public void stopWorker(TranscodeJob transcodeJob) {
    String workerKey = this.getWorkerKey(transcodeJob);
    TranscodeWorkerHandle handle = workers.remove(workerKey);
    if (handle != null) {
      Process ffmpeg = handle.getFfmpegProcess();
      if (ffmpeg != null && ffmpeg.isAlive()) {
        ffmpeg.destroyForcibly();
      }

      handle.getFuture().cancel(true);

      handle.getFirstSegmentReadyFuture().completeExceptionally(new CancellationException("Transcoding stopped."));
    }
  }
}