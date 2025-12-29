package dev.amethyst.app.messaging.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.amethyst.app.chat.event.AbstractChatEvent;
import dev.amethyst.app.logging.Logging;
import dev.amethyst.app.messaging.dto.ErrorResponse;
import dev.amethyst.app.messaging.model.AbstractWebSocketMessage;
import dev.amethyst.app.messaging.model.BaseMessageType;
import dev.amethyst.app.messaging.service.MessagePublisher;
import dev.amethyst.app.player.command.AbstractPlayerCommand;
import dev.amethyst.app.room.command.AbstractRoomCommand;
import dev.amethyst.app.session.service.SessionRegistry;
import dev.amethyst.app.session.service.SessionWrapper;
import dev.amethyst.app.user.event.ConnectEvent;
import dev.amethyst.app.user.event.DisconnectEvent;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Handle incoming websocket messages (Commands)
 */
@Component
public class IncomingWsMessageHandler extends TextWebSocketHandler {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private MessagePublisher messagePublisher;
  @Autowired
  private SessionRegistry sessionRegistry;

  @Override
  public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws IOException {
    try {
      JsonNode node = mapper.readTree(message.getPayload());
      AbstractWebSocketMessage payload;

      BaseMessageType messageType = BaseMessageType.valueOf(node.get("type").asText());

      switch (messageType) {
        case BaseMessageType.PLAYER:
          payload = mapper.readValue(message.getPayload(), AbstractPlayerCommand.class);
          break;
        case BaseMessageType.ROOM:
          payload = mapper.readValue(message.getPayload(), AbstractRoomCommand.class);
          break;
        case BaseMessageType.CHAT:
          payload = mapper.readValue(message.getPayload(), AbstractChatEvent.class);
          break;
        default:
          throw new IllegalArgumentException("Invalid type.");
      }

      messagePublisher.publish(session, payload);
    } catch (JsonProcessingException e) {
      Logging.error("Unable to handle event\n" + e.getMessage());
      TextMessage error = ErrorResponse.builder().message("Bad message.").build().convertToTextMessage();
      session.sendMessage(error);
    } catch (IllegalArgumentException e) {
      Logging.error(e.getMessage());
      TextMessage error = ErrorResponse.builder().message("Invalid type.").build().convertToTextMessage();
      session.sendMessage(error);
    }
  }

  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession session) {
    Logging.info("Session connected.");
    sessionRegistry.register(new SessionWrapper(session));
    ConnectEvent event = ConnectEvent.builder().session(sessionRegistry.get(session.getId())).build();
    messagePublisher.publish(session, event);
  }

  @Override
  public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
    Logging.info("Session disconnected.");
    DisconnectEvent event = DisconnectEvent.builder().session(sessionRegistry.get(session.getId())).build();
    messagePublisher.publish(session, event);
    sessionRegistry.unregister(new SessionWrapper(session));
  }

}
