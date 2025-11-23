package dev.seanboaden.hls.room.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.seanboaden.hls.events.AbstractBaseEvent;
import dev.seanboaden.hls.events.BaseEventType;
import dev.seanboaden.hls.room.enums.RoomEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomCreateEvent.class, name = RoomEventType.ROOM_CREATE),
        @JsonSubTypes.Type(value = RoomLeaveEvent.class, name = RoomEventType.ROOM_LEAVE),
        @JsonSubTypes.Type(value = RoomJoinEvent.class, name = RoomEventType.ROOM_JOIN)
})
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractRoomEvent extends AbstractBaseEvent {
    private String eventType;
    private String roomCode;

    {
        setType(BaseEventType.ROOM);
    }
}
