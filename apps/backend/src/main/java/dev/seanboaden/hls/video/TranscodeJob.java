package dev.seanboaden.hls.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.video.QualityProfiles.QualityProfile;

@Data
@Builder
@AllArgsConstructor
public class TranscodeJob {
  @Nullable
  private String roomCode;
  @NonNull
  private Media media;
  @NonNull
  private QualityProfile quality;
  @Builder.Default
  @NonNull
  private JobType type = JobType.HLS;

  /**
   * optional - provide one of either fromSegmentName or fromTimestampSeconds
   */
  @Nullable
  private String fromSegmentName;
  /**
   * optional - provide one of either fromSegmentName or fromTimestampSeconds
   */
  @Nullable
  private Double fromTimestampSeconds;

  /**
   * optional - omitting this parameter will make ffmpeg run until transcode
   * completes on the file
   */
  @Nullable
  private Long runForSegmentAmount;

  public enum JobType {
    HLS,
    MPEG_DASH
  }
}
