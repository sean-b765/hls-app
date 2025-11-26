package dev.seanboaden.hls.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import dev.seanboaden.hls.session.SessionRegistry;
import dev.seanboaden.hls.session.SessionWrapper;

@Component
public class EventPublisher {
  @Autowired
  private ApplicationEventPublisher eventPublisher;
  @Autowired
  private SessionRegistry sessionRegistry;

  public void publish(SessionWrapper session, AbstractWebSocketEvent event) {
    event.setSession(session);
    eventPublisher.publishEvent(event);
  }

  public void publish(WebSocketSession session, AbstractWebSocketEvent event) {
    SessionWrapper sessionWrapper = sessionRegistry.get(session.getId());
    event.setSession(sessionWrapper);
    eventPublisher.publishEvent(event);
  }
}
