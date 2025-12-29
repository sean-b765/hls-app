package dev.amethyst.app.player.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.messaging.model.BaseMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "kind", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PlayerChooseMediaCommand.class),
    @JsonSubTypes.Type(value = PlayerSeekCommand.class),
    @JsonSubTypes.Type(value = PlayerStateChangeCommand.class),
})
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractPlayerCommand extends AbstractWebSocketMessage {
  @Override
  public BaseMessageType getType() {
    return BaseMessageType.PLAYER;
  }
}
