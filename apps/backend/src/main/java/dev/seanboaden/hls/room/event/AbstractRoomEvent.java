package dev.seanboaden.hls.room.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.messaging.model.BaseMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RoomCreatedEvent.class),
    @JsonSubTypes.Type(value = RoomLeftEvent.class),
    @JsonSubTypes.Type(value = RoomJoinedEvent.class)
})
public abstract class AbstractRoomEvent extends AbstractWebSocketMessage {
  @Override
  public BaseMessageType getType() {
    return BaseMessageType.ROOM;
  }
}
