package dev.seanboaden.hls.transcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.FfmpegService;
import dev.seanboaden.hls.lib.FileSystemService;

@Service
public class TranscodeManager {
  @Autowired
  private FfmpegService ffmpegService;
  @Autowired
  private TranscodingService transcodingService;
  @Autowired
  private FileSystemService fileSystemService;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final ConcurrentHashMap<String, TranscodeWorkerHandle> workerPool = new ConcurrentHashMap<>();

  private String getWorkerKey(TranscodeJob transcodeJob) {
    return StringUtils.joinWith(
        ":",
        transcodeJob.getType().name(),
        transcodeJob.getRoomCode(),
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName());
  }

  public Path getSegmentPath(TranscodeJob transcodeJob) {
    String segmentPath = this.fileSystemService.getSegmentDirectory(transcodeJob);
    return Paths.get(segmentPath, transcodeJob.getFromSegmentName());
  }

  /**
   * Wait for the requested segment file to exist, polling periodically until
   * timeout.
   */
  public CompletableFuture<Void> waitForSegment(TranscodeJob transcodeJob, long timeoutMs) {
    CompletableFuture<Void> segmentReadyFuture = new CompletableFuture<>();
    Path segmentPath = getSegmentPath(transcodeJob);

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

  public boolean shouldTranscode(TranscodeJob transcodeJob) {
    // Retrieve the required segment length
    long requestedSegmentNumber = this.transcodingService.extractSegmentNumber(transcodeJob.getFromSegmentName());
    long segmentRunLength = this.transcodingService.calculateSegmentRunLength(
        requestedSegmentNumber,
        transcodeJob.getFromSegmentName());

    if (segmentRunLength <= 0)
      return false;

    long toSegment = requestedSegmentNumber + segmentRunLength;
    // If the file doesn't exist, we need to transcode
    boolean fileExists = Files.exists(this.getSegmentPath(transcodeJob));
    String segmentEndName = this.transcodingService.toSegmentName(toSegment);

    if (!fileExists) {
      transcodeJob.setEndSegmentName(segmentEndName);
    }

    return !fileExists;
  }

  public CompletableFuture<Void> startOrRetrieveWorker(TranscodeJob transcodeJob) {
    String workerKey = this.getWorkerKey(transcodeJob);

    System.out.println("[STATUS]: " + this.workerPool.size() + " workers running");
    return workerPool
        .compute(workerKey, (key, worker) -> {
          if (worker != null && !worker.getFuture().isDone()) {
            // Already working
            System.out.println("[" + workerKey + "]: worker already running");
            return worker;
          }
          System.out.println(
              "[" + workerKey + "]: starting worker for " + transcodeJob.getFromSegmentName() + " -> "
                  + transcodeJob.getEndSegmentName());

          // New worker is needed
          CompletableFuture<Void> firstSegmentReadyFuture = new CompletableFuture<>();
          CompletableFuture<Void> allSegmentsReadyFuture = new CompletableFuture<>();
          String outputPath = this.fileSystemService.getSegmentDirectory(transcodeJob);

          TranscodeWorker workerThread = new TranscodeWorker(
              ffmpegService,
              transcodingService,
              fileSystemService,
              transcodeJob,
              outputPath,
              firstSegmentReadyFuture,
              allSegmentsReadyFuture);
          Future<?> future = threadPool.submit(workerThread);

          TranscodeWorkerHandle handle = TranscodeWorkerHandle.builder()
              .firstSegmentReadyFuture(firstSegmentReadyFuture)
              .allSegmentsReadyFuture(allSegmentsReadyFuture)
              .future(future)
              .build();

          // Evict from pool when the future completes
          allSegmentsReadyFuture.whenComplete((result, exception) -> {
            System.out.println("evicting " + key);
            workerPool.remove(key);
          });

          return handle;
        })
        .getFirstSegmentReadyFuture();
  }

  public void stopWorker(TranscodeJob transcodeJob) {
    String workerKey = this.getWorkerKey(transcodeJob);
    TranscodeWorkerHandle handle = workerPool.remove(workerKey);
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