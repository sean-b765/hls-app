package dev.seanboaden.hls.player.events;

import dev.seanboaden.hls.player.enums.PlayerEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChooseTrackEvent extends AbstractPlayerEvent {
  private String trackId;

  {
    setEventType(PlayerEventType.CHOOSE_TRACK);
  }
}
