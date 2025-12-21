package dev.seanboaden.hls.messaging.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.chat.event.AbstractChatEvent;
import dev.seanboaden.hls.logging.Logging;
import dev.seanboaden.hls.messaging.dto.ErrorResponse;
import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.messaging.model.BaseMessageType;
import dev.seanboaden.hls.messaging.service.MessagePublisher;
import dev.seanboaden.hls.player.command.AbstractPlayerCommand;
import dev.seanboaden.hls.room.command.AbstractRoomCommand;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.session.service.SessionWrapper;
import dev.seanboaden.hls.user.event.ConnectEvent;
import dev.seanboaden.hls.user.event.DisconnectEvent;

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
