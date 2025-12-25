package dev.seanboaden.hls.media.event;

import dev.seanboaden.hls.media.model.Media;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaCreatedEvent {
  private Media media;
}
