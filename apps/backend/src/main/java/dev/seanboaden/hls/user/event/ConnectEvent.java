package dev.seanboaden.hls.user.event;

import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.messaging.model.BaseMessageType;
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
