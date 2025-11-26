package dev.seanboaden.hls.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SessionRegistry {
  private final ConcurrentMap<String, SessionWrapper> sessions = new ConcurrentHashMap<>();

  public void register(SessionWrapper session) {
    sessions.put(session.getId(), session);
  }

  public void unregister(SessionWrapper session) {
    sessions.remove(session.getId());
  }

  public SessionWrapper get(String sessionId) {
    return sessions.get(sessionId);
  }
}
