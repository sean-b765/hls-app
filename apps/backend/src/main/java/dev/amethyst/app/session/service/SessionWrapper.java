package dev.amethyst.app.session.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.amethyst.app.logging.Logging;
import dev.amethyst.app.messaging.dto.ErrorResponse;
import dev.amethyst.app.messaging.dto.InfoResponse;
import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.user.model.User;
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
    this.sendMessageSafe(new TextMessage(message));
  }

  private void sendMessageSafe(TextMessage message) {
    if (message == null)
      return;
    try {
      // lock session when sending message to ensure our crazy updates don't get this
      // error:
      // java.lang.IllegalStateException: The remote endpoint was in state
      // [TEXT_PARTIAL_WRITING] which is an invalid state for called method
      // TODO: a faster way may be to queue messages? idfk
      session.sendMessage(message);
    } catch (IOException e) {
      Logging.error("Unable to send message");
    }
  }

  public String getId() {
    return session.getId();
  }
}
