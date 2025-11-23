package dev.seanboaden.hls.room.events;

import static dev.seanboaden.hls.room.enums.RoomEventType.ROOM_JOINED;

import dev.seanboaden.hls.room.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoomJoinedEvent extends AbstractRoomEvent {
  private Room room;

  {
    setEventType(ROOM_JOINED);
  }
}
