package dev.amethyst.app.player.event;

import dev.amethyst.app.player.model.PlayerStateType;
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
