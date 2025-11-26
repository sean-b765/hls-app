package dev.seanboaden.hls.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import dev.seanboaden.hls.session.SessionWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractWebSocketEvent extends AbstractTimestampedEvent {
  /**
   * This comes from my SessionEventMiddleware listener ...
   */
  @JsonIgnore
  private SessionWrapper session;
}
