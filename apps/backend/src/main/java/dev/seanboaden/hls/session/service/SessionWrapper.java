package dev.seanboaden.hls.session.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.logging.Logging;
import dev.seanboaden.hls.messaging.dto.ErrorResponse;
import dev.seanboaden.hls.messaging.dto.InfoResponse;
import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@Data
public class SessionWrapper {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private WebSocketSession session;

  public SessionWrapper(WebSocketSession session) {
    this.session = session;
    this.session.getAttributes().put("userId", UUID.randomUUID().toString());
  }

  public void sendError(String message) {
    this.sendMessageSafe(ErrorResponse.builder().message(message).build().convertToTextMessage());
  }

  public void sendInfo(String message) {
    this.sendMessageSafe(InfoResponse.builder().message(message).build().convertToTextMessage());
  }

  public void sendEvent(AbstractWebSocketMessage event) {
    this.sendMessageSafe(event.convertToTextMessage());
  }

  public void sendMessage(Object message) {
    try {
      this.sendMessageSafe(objectMapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      Logging.error("Unable to serialize message");
    }
  }

  public String getUserId() {
    Object userId = this.session.getAttributes().get("userId");
    if (userId == null)
      return null;
    return userId.toString();
  }

  private void sendMessageSafe(String message) {
    try {
      session.sendMessage(new TextMessage(message));
    } catch (IOException e) {
      Logging.error("Unable to send message");
    }
  }

  private void sendMessageSafe(TextMessage message) {
    try {
      session.sendMessage(message);
    } catch (IOException e) {
      Logging.error("Unable to send message");
    }
  }

  public String getId() {
    return session.getId();
  }
}
