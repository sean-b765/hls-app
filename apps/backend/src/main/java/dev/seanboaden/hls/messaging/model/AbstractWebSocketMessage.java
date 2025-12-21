package dev.seanboaden.hls.messaging.model;

import java.time.Instant;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.logging.Logging;
import dev.seanboaden.hls.session.service.SessionWrapper;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractWebSocketMessage {
  @JsonIgnore
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * This comes from SessionEventMiddleware listener ...
   */
  @JsonIgnore
  private SessionWrapper session;
  @Builder.Default
  private String timestamp = Instant.now().toString();
  private BaseMessageType type;
  private String kind;

  public abstract BaseMessageType getType();

  public String getUserId() {
    if (this.session == null)
      return null;
    return this.session.getUserId();
  }

  @JsonIgnore
  public TextMessage convertToTextMessage() {
    try {
      return new TextMessage(objectMapper.writeValueAsString(this));
    } catch (JsonProcessingException e) {
      Logging.info("ERROR WHILE CONVERTING");
      return null;
    }
  }
}
