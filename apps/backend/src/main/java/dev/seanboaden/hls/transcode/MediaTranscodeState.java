package dev.seanboaden.hls.transcode;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MediaTranscodeState {
  @Builder.Default
  private ConcurrentSkipListMap<Long, TranscodeStatusEnum> segmentAvailability = new ConcurrentSkipListMap<>();
  private LocalDateTime lastTranscodeTimestamp;
}
