package dev.seanboaden.hls.transcode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

@Service
public class TranscodeManager {
  @Autowired
  private FfmpegService ffmpegService;
  @Autowired
  private TranscodingService transcodingService;
  @Autowired
  private FileSystemService fileSystemService;
  @Autowired
  private ObjectMapper objectMapper;

  private final ExecutorService threadPool = Executors.newCachedThreadPool();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final ConcurrentHashMap<String, TranscodeWorkerHandle> workerPool = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, MediaTranscodeState> mediaTranscodeStates = new ConcurrentHashMap<>();

  private String getWorkerKey(TranscodeJob transcodeJob) {
    String tenant = "";
    if (transcodeJob.getRoomCode() == null)
      tenant = transcodeJob.getUserId();
    else
      tenant = transcodeJob.getRoomCode();

    return StringUtils.joinWith(
        ":",
        transcodeJob.getType().name(),
        tenant,
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName());
  }

  /**
   * ensures the "endSegmentName" on the TranscodeJob
   * 
   * @param transcodeJob
   */
  public void ensureEndSegmentName(TranscodeJob transcodeJob) {
    // Retrieve the required segment length
    long requestedSegmentNumber = this.transcodingService.extractSegmentNumber(transcodeJob.getFromSegmentName());
    MediaTranscodeState mediaTranscodeState = this.getMediaTranscodeState(transcodeJob);
    long segmentCountLimit = this.transcodingService.getSegmentCountLimit();
    long defaultEndSegment = requestedSegmentNumber + segmentCountLimit;
    ConcurrentSkipListMap<Long, TranscodeStatusEnum> segmentMap = mediaTranscodeState.getSegmentAvailability();

    if (segmentMap.isEmpty()) {
      // default limit
      transcodeJob.setEndSegmentName(this.transcodingService.toSegmentName(defaultEndSegment));
      return;
    }

    // Look for the next segment which exists in our skiplistmap
    Long nextProcessedSegment = segmentMap.higherKey(requestedSegmentNumber);
    if (nextProcessedSegment == null) {
      // default limit
      transcodeJob.setEndSegmentName(this.transcodingService.toSegmentName(defaultEndSegment));
      return;
    }

    // Clamp to defaultEndSegment, but transcode until the next segment
    long endSegment = nextProcessedSegment.longValue() - 1;
    if (endSegment > defaultEndSegment) {
      transcodeJob.setEndSegmentName(this.transcodingService.toSegmentName(defaultEndSegment));
      return;
    }
    transcodeJob.setEndSegmentName(this.transcodingService.toSegmentName(endSegment));
  }

  /**
   * Wait for the requested segment file to exist, polling periodically until
   * timeout.
   */
  public CompletableFuture<Void> waitForSegment(TranscodeJob transcodeJob, long timeoutMs) {
    CompletableFuture<Void> segmentReadyFuture = new CompletableFuture<>();

    final long start = System.currentTimeMillis();
    final long pollIntervalMs = 200L;

    final ScheduledFuture<?> scheduled = scheduler.scheduleAtFixedRate(() -> {
      try {
        if (this.isSegmentComplete(transcodeJob)) {
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

  public CompletableFuture<Void> startOrRetrieveWorker(TranscodeJob transcodeJob) {
    String workerKey = this.getWorkerKey(transcodeJob);

    System.out.println("[STATUS]: " + this.workerPool.size() + " workers running");
    return workerPool
        .compute(workerKey, (key, worker) -> {
          if (worker != null && !worker.getFuture().isDone()) {
            // Already working
            return worker;
          }
          System.out.println(
              "[" + workerKey + "]: starting worker for " + transcodeJob.getFromSegmentName() + " -> "
                  + transcodeJob.getEndSegmentName());

          // New worker is needed
          CompletableFuture<Void> firstSegmentReadyFuture = new CompletableFuture<>();
          CompletableFuture<Void> allSegmentsReadyFuture = new CompletableFuture<>();

          this.setQueued(transcodeJob);
          TranscodeWorker workerThread = new TranscodeWorker(
              ffmpegService,
              transcodingService,
              fileSystemService,
              transcodeJob,
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
            workerPool.remove(key);
            this.setComplete(transcodeJob);
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

  public boolean isSegmentComplete(TranscodeJob transcodeJob) {
    MediaTranscodeState mediaTranscodeState = this.getMediaTranscodeState(transcodeJob);
    long segment = this.transcodingService.extractSegmentNumber(transcodeJob.getFromSegmentName());
    if (!mediaTranscodeState.getSegmentAvailability().containsKey(segment))
      return false;
    TranscodeStatusEnum transcodeStatus = mediaTranscodeState.getSegmentAvailability().get(segment);
    if (transcodeStatus == null)
      return false;
    return transcodeStatus.equals(TranscodeStatusEnum.COMPLETE);
  }

  private void setQueued(TranscodeJob transcodeJob) {
    MediaTranscodeState mediaTranscodeState = this.getMediaTranscodeState(transcodeJob);

    long startPoint = this.transcodingService.extractSegmentNumber(transcodeJob.getFromSegmentName());
    long endPoint = this.transcodingService.extractSegmentNumber(transcodeJob.getEndSegmentName());

    for (long segment = startPoint; segment <= endPoint; segment++) {
      mediaTranscodeState.getSegmentAvailability().put(segment, TranscodeStatusEnum.QUEUED);
    }
  }

  private void setComplete(TranscodeJob transcodeJob) {
    MediaTranscodeState mediaTranscodeState = this.getMediaTranscodeState(transcodeJob);

    long startPoint = this.transcodingService.extractSegmentNumber(transcodeJob.getFromSegmentName());
    long endPoint = this.transcodingService.extractSegmentNumber(transcodeJob.getEndSegmentName());

    for (long segment = startPoint; segment <= endPoint; segment++) {
      mediaTranscodeState.getSegmentAvailability().put(segment, TranscodeStatusEnum.COMPLETE);
    }
    mediaTranscodeState.setLastTranscodeTimestamp(LocalDateTime.now());
  }

  private MediaTranscodeState getMediaTranscodeState(TranscodeJob transcodeJob) {
    return this.mediaTranscodeStates.computeIfAbsent(
        transcodeJob.getMedia().getId(),
        (key) -> MediaTranscodeState.builder().build());
  }
}