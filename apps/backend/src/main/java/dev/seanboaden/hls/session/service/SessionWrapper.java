package dev.seanboaden.hls.session.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.logging.Logging;
import dev.seanboaden.hls.messaging.dto.ErrorResponse;
import dev.seanboaden.hls.messaging.dto.InfoResponse;
import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.user.model.User;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Data
public class SessionWrapper {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private WebSocketSession session;

  public SessionWrapper(WebSocketSession session) {
    this.session = session;
  }

  public void sendError(String message) {
    this.sendMessageSafe(ErrorResponse.builder().message(message).build().convertToTextMessage());
  }

  public void sendInfo(String message) {
    this.sendMessageSafe(InfoResponse.builder().message(message).build().convertToTextMessage());
  }

  public void sendEvent(AbstractWebSocketMessage event) {
    if (event.getSession() == null)
      event.setSession(this);
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
    User user = this.getUser();
    if (user == null)
      return null;
    return user.getId();
  }

  public User getUser() {
    Object userAttribute = this.session.getAttributes().get("user");
    if (userAttribute == null)
      return null;
    return (User) userAttribute;
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
