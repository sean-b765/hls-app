package dev.seanboaden.hls.room.event;

import dev.seanboaden.hls.room.command.AbstractRoomCommand;
import dev.seanboaden.hls.room.model.LeaveReason;
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
