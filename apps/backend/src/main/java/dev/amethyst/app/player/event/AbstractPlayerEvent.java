package dev.amethyst.app.player.event;

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
    @JsonSubTypes.Type(value = PlayerChoseMediaEvent.class),
    @JsonSubTypes.Type(value = PlayerSeekedEvent.class),
    @JsonSubTypes.Type(value = PlayerStateChangedEvent.class),
})
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractPlayerEvent extends AbstractWebSocketMessage {
  @Override
  public BaseMessageType getType() {
    return BaseMessageType.PLAYER;
  }
}
