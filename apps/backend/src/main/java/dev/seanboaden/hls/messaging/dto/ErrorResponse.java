package dev.seanboaden.hls.messaging.dto;

import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.messaging.model.BaseMessageType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends AbstractWebSocketMessage {
  private String message;

  @Override
  public BaseMessageType getType() {
    return BaseMessageType.ERROR;
  }
}