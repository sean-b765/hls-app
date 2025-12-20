package dev.seanboaden.hls.room.event;

import dev.seanboaden.hls.room.model.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoomCreatedEvent extends AbstractRoomEvent {
  private Room room;
}