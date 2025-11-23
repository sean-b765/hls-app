package dev.seanboaden.hls.room.events;

import static dev.seanboaden.hls.room.enums.RoomEventType.ROOM_LEAVE;

import dev.seanboaden.hls.room.enums.LeaveReason;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoomLeaveEvent extends AbstractRoomEvent {
  private LeaveReason reason;

  {
    setEventType(ROOM_LEAVE);
  }
}
