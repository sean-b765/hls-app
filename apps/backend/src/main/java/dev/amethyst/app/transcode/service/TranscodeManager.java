package dev.amethyst.app.transcode.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.amethyst.app.filesystem.service.FileSystemService;
import dev.amethyst.app.lib.service.FfmpegService;
import dev.amethyst.app.transcode.model.TranscodeJob;
import dev.amethyst.app.transcode.model.TranscodeWorkerHandle;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

@Service
public class TranscodeManager {
  @Autowired
  @Getter
  private FfmpegService ffmpegService;
  @Autowired
  @Getter
  private TranscodingService transcodingService;
  @Autowired
  @Getter
  private FileSystemService fileSystemService;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();
  /**
   * TODO this shouldn't be single threaded as it will greatly impact performance
   * for multi clients
   */
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final ConcurrentHashMap<String, TranscodeWorkerHandle> workerPool = new ConcurrentHashMap<>();
  private final Set<Process> ffmpegProcesses = ConcurrentHashMap.newKeySet();

  @PreDestroy
  public void shutdown() {
    this.ffmpegProcesses.forEach(Process::destroyForcibly);
  }

  public void registerFfmpegProcess(Process process) {
    this.ffmpegProcesses.add(process);
  }

  /**
   * Wait for the requested segment file to exist, polling periodically until
   * timeout.
   */
  public CompletableFuture<Void> waitForSegment(TranscodeJob transcodeJob, long timeoutMs) {
    CompletableFuture<Void> segmentReadyFuture = new CompletableFuture<>();
    Path segmentPath = this.fileSystemService.getSegmentPath(transcodeJob);

    final long start = System.currentTimeMillis();
    final long pollIntervalMs = 200L;

    final ScheduledFuture<?> scheduled = scheduler.scheduleAtFixedRate(() -> {
      try {
        if (Files.exists(segmentPath)) {
          segmentReadyFuture.complete(null);
        } else if (System.currentTimeMillis() - start >= timeoutMs) {
          segmentReadyFuture.completeExceptionally(new Exception("File Not Found"));
        }
      } catch (Exception ex) {
        segmentReadyFuture.completeExceptionally(ex);
      }
    }, 0, pollIntervalMs, TimeUnit.MILLISECONDS);

    segmentReadyFuture.whenComplete((r, ex) -> scheduled.cancel(false));
    return segmentReadyFuture;
  }

  public Future<?> ensureWorker(TranscodeJob transcodeJob) {
    String workerKey = transcodeJob.getWorkerKey();

    System.out.println("[STATUS]: " + this.workerPool.size() + " workers running");
    return workerPool
        .compute(workerKey, (key, worker) -> {
          if (worker != null && !worker.getFuture().isDone()) {
            // Already working
            return worker;
          }
          // New worker is needed
          System.out.println("[" + workerKey + "]: starting worker from " + transcodeJob.getFromSegmentName());

          TranscodeWorker workerThread = new TranscodeWorker(this, transcodeJob);
          Future<?> future = threadPool.submit(workerThread);

          TranscodeWorkerHandle handle = TranscodeWorkerHandle.builder()
              .future(future)
              .build();
          return handle;
        })
        .getFuture();
  }

  /**
   * Call if seeking to kill ffmpeg process thread
   */
  public void stopWorker(TranscodeJob transcodeJob) {
    String workerKey = transcodeJob.getWorkerKey();
    TranscodeWorkerHandle handle = workerPool.remove(workerKey);
    if (handle != null) {
      Process ffmpeg = handle.getFfmpegProcess();
      if (ffmpeg != null && ffmpeg.isAlive()) {
        ffmpeg.destroyForcibly();
      }

      handle.getFuture().cancel(true);
    }
  }
}