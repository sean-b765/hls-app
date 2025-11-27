package dev.seanboaden.hls.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SessionRegistry {
  private final ConcurrentMap<String, SessionWrapper> sessions = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, String> userIdToSessionIdMap = new ConcurrentHashMap<>();

  public void register(SessionWrapper session) {
    String userId = session.getUserId();
    sessions.put(session.getId(), session);
    if (userId != null) {
      userIdToSessionIdMap.put(userId, session.getId());
    }
  }

  public void unregister(SessionWrapper session) {
    String userId = session.getUserId();
    sessions.remove(session.getId());
    if (userId != null) {
      userIdToSessionIdMap.remove(userId);
    }
  }

  public SessionWrapper get(String sessionId) {
    return sessions.get(sessionId);
  }

  public SessionWrapper getByUserId(String userId) {
    if (userId == null || !userIdToSessionIdMap.containsKey(userId))
      return null;
    String sessionId = userIdToSessionIdMap.get(userId);
    if (sessionId == null)
      return null;
    return this.get(sessionId);
  }
}
