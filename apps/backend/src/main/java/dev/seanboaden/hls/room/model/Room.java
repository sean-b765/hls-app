package dev.seanboaden.hls.room.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.seanboaden.hls.messaging.model.AbstractWebSocketMessage;
import dev.seanboaden.hls.session.service.SessionWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
  private String code;
  private String name;
  @JsonIgnore
  private String password;
  @Builder.Default
  @JsonIgnore
  private Map<String, SessionWrapper> sessions = new HashMap<>();

  public void broadcastEvent(AbstractWebSocketMessage event) {
    for (SessionWrapper session : sessions.values()) {
      session.sendEvent(event);
    }
  }

  public void leave(SessionWrapper session) {
    sessions.remove(session.getId());
  }

  public void join(SessionWrapper session) {
    sessions.put(session.getId(), session);
  }
}
