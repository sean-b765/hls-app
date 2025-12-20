package dev.seanboaden.hls.room.command;

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
    @JsonSubTypes.Type(value = RoomCreateCommand.class),
    @JsonSubTypes.Type(value = RoomLeaveCommand.class),
    @JsonSubTypes.Type(value = RoomJoinCommand.class)
})
public abstract class AbstractRoomCommand extends AbstractWebSocketMessage {
  @Override
  public BaseMessageType getType() {
    return BaseMessageType.ROOM;
  }
}
