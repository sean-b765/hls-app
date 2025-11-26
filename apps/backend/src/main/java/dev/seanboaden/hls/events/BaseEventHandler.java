package dev.seanboaden.hls.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.chat.events.ChatEvent;
import dev.seanboaden.hls.logging.Logging;
import dev.seanboaden.hls.player.events.AbstractPlayerEvent;
import dev.seanboaden.hls.room.events.AbstractRoomEvent;
import dev.seanboaden.hls.session.SessionRegistry;
import dev.seanboaden.hls.session.SessionWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class BaseEventHandler extends TextWebSocketHandler {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private EventPublisher eventPublisher;
  @Autowired
  private SessionRegistry sessionRegistry;

  @Override
  public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
    try {
      JsonNode node = mapper.readTree(message.getPayload());
      AbstractBaseEvent payload;

      switch (node.get("type").asText()) {
        case BaseEventType.PLAYER:
          payload = mapper.readValue(message.getPayload(), AbstractPlayerEvent.class);
          break;
        case BaseEventType.ROOM:
          payload = mapper.readValue(message.getPayload(), AbstractRoomEvent.class);
          break;
        case BaseEventType.CHAT:
          payload = mapper.readValue(message.getPayload(), ChatEvent.class);
          break;
        default:
          throw new IllegalAccessException("Unknown type");
      }

      eventPublisher.publish(session, payload);
    } catch (JsonProcessingException e) {
      Logging.error("Unable to handle event\n" + e.getMessage());
    } catch (IllegalAccessException e) {
      Logging.error(e.getMessage());
    }
  }

  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession session) {
    Logging.info("Session connected.");
    sessionRegistry.register(new SessionWrapper(session));
    ConnectEvent event = ConnectEvent.builder().session(sessionRegistry.get(session.getId())).build();
    eventPublisher.publish(session, event);
  }

  @Override
  public void afterConnectionClosed(@NonNull WebSocketSession session,
      @NonNull CloseStatus status) {
    Logging.info("Session disconnected.");
    DisconnectEvent event = DisconnectEvent.builder().session(sessionRegistry.get(session.getId())).build();
    eventPublisher.publish(session, event);
    sessionRegistry.unregister(new SessionWrapper(session));
  }

}
