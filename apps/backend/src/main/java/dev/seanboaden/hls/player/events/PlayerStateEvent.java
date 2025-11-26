package dev.seanboaden.hls.player.events;

import dev.seanboaden.hls.player.enums.PlayerEventType;
import dev.seanboaden.hls.player.enums.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayerStateEvent extends AbstractPlayerEvent {
  private PlayerState state;

  {
    setEventType(PlayerEventType.PLAYER_STATE);
  }
}
