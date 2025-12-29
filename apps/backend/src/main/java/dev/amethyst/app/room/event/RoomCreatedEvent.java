package dev.amethyst.app.room.event;

import dev.amethyst.app.room.model.Room;
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