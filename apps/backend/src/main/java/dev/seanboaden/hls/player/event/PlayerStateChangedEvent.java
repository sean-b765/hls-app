package dev.seanboaden.hls.player.event;

import dev.seanboaden.hls.player.model.PlayerStateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayerStateChangedEvent extends AbstractPlayerEvent {
  private PlayerStateType state;
}
