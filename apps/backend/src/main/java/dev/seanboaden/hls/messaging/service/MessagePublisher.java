package dev.seanboaden.hls.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.session.service.SessionRegistry;
import dev.seanboaden.hls.session.service.SessionWrapper;

/**
 * Dispatches messages to their handlers
 */
@Component
public class MessagePublisher {
  @Autowired
  private ApplicationEventPublisher publisher;
  @Autowired
  private SessionRegistry sessionRegistry;

  public void publish(SessionWrapper session, AbstractWebSocketMessage event) {
    event.setSession(session);
    publisher.publishEvent(event);
  }

  public void publish(WebSocketSession session, AbstractWebSocketMessage event) {
    SessionWrapper sessionWrapper = sessionRegistry.get(session.getId());
    event.setSession(sessionWrapper);
    publisher.publishEvent(event);
  }
}
