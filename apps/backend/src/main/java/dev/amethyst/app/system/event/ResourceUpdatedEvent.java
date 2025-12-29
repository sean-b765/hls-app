package dev.amethyst.app.system.event;

import com.fasterxml.jackson.annotation.JsonInclude;

import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.messaging.model.BaseMessageType;
import dev.amethyst.app.system.lib.Endpoints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Supe simple event for fetching a newly created or updated resource
 */
@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class ResourceUpdatedEvent extends AbstractWebSocketMessage {
  private String id;
  private Endpoints uri;

  @Override
  public BaseMessageType getType() {
    return BaseMessageType.RESOURCE_UPDATED;
  }

  @Override
  public String getUserId() {
    return null;
  }
}
