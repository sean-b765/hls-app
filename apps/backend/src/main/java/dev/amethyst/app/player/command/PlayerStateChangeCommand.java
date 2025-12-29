package dev.amethyst.app.player.command;

import dev.amethyst.app.player.model.PlayerStateType;
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
public class PlayerStateChangeCommand extends AbstractPlayerCommand {
  private PlayerStateType state;
}
