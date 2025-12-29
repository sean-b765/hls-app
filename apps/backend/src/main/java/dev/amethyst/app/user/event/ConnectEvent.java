package dev.amethyst.app.user.event;

import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.messaging.model.BaseMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ConnectEvent extends AbstractWebSocketMessage {
  @Override
  public BaseMessageType getType() {
    return BaseMessageType.INFO;
  }
}
