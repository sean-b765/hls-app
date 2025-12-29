package dev.amethyst.app.messaging.dto;

import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.messaging.model.BaseMessageType;
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