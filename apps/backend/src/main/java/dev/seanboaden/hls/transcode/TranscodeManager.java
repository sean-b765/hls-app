package dev.seanboaden.hls.transcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.lib.FfmpegService;
import dev.seanboaden.hls.lib.FileSystemService;
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
  @Autowired
  private ObjectMapper objectMapper;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final ConcurrentHashMap<String, TranscodeWorkerHandle> workerPool = new ConcurrentHashMap<>();
  private final Set<Process> ffmpegProcesses = ConcurrentHashMap.newKeySet();

  private String getWorkerKey(TranscodeJob transcodeJob) {
    String tenant = "";
    if (transcodeJob.getRoomCode() == null)
      tenant = transcodeJob.getUserId();
    else
      tenant = transcodeJob.getRoomCode();

    return StringUtils.joinWith(
        ":",
        transcodeJob.getType().name(),
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName(),
        tenant);
  }

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
    String workerKey = this.getWorkerKey(transcodeJob);

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
    String workerKey = this.getWorkerKey(transcodeJob);
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