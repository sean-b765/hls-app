package dev.amethyst.app.transcode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.video.model.QualityProfiles.QualityProfile;

@Data
@Builder
@AllArgsConstructor
public class TranscodeJob {
  /**
   * optional - provide one of either roomCode or userId
   */
  @Nullable
  private String roomCode;
  /**
   * optional - provide one of either roomCode or userId
   */
  @Nullable
  private String userId;
  @NonNull
  private Media media;
  @NonNull
  private QualityProfile quality;
  @Builder.Default
  @NonNull
  private JobType type = JobType.HLS;
  @NonNull
  private String fromSegmentName;

  public enum JobType {
    HLS,
    MPEG_DASH
  }

  public String getWorkerKey() {
    String tenant = "";
    if (this.getRoomCode() == null)
      tenant = this.getUserId();
    else
      tenant = this.getRoomCode();

    return StringUtils.joinWith(
        ":",
        this.getType().name(),
        this.getMedia().getId(),
        this.getQuality().getName(),
        tenant);
  }
}
