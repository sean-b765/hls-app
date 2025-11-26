package dev.seanboaden.hls.video;

import java.util.concurrent.Callable;

/**
 * <code>
 * ffmpeg -i "input.mkv" -preset superfast -map 0:v -map 0:a -codec:v libx264 -profile:v high -level:v 4.0 -b:v 1000k -codec:a aac -ac 2 -b:a 128k -f hls -hls_time 3 -hls_playlist_type vod -hls_segment_filename "segment%05d.ts" -hls_segment_type mpegts -pix_fmt yuv420p master.m3u8
 * -force_key_frames "expr:gte(t,n_forced*4)" to ensure segments align with extracted playlist
 * </code>
 */

public class TranscodeWorker implements Callable<VideoChunk> {
  private static final String outputDir = "./";
  private final TranscodeJob job;

  public TranscodeWorker(TranscodeJob job) {
    this.job = job;
  }

  @Override
  public VideoChunk call() throws Exception {
    switch (job.getType()) {
      case HLS:
        startHls(job.getInputFilePath(), job.getTimestampMs());

      default:
        throw new IllegalArgumentException("Invalid job type");
    }
  }

  private void startHls(String file, long timestamp) throws Exception {
    ffmpegOutputToHls(file, timestamp);
  }

  // Placeholder test
  private void ffmpegOutputToHls(String inputFile, long timestampMs) {
    String outputFile = outputDir + "/master.m3u8";
    // transcode
  }
}
