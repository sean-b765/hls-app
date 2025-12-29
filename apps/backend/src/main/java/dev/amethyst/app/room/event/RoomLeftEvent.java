package dev.amethyst.app.room.event;

import dev.amethyst.app.room.command.AbstractRoomCommand;
import dev.amethyst.app.room.model.LeaveReason;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoomLeftEvent extends AbstractRoomCommand {
  private LeaveReason reason;
}
