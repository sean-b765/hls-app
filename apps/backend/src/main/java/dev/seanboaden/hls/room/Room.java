package dev.seanboaden.hls.room;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.seanboaden.hls.chat.Chat;
import dev.seanboaden.hls.events.AbstractBaseEvent;
import dev.seanboaden.hls.session.SessionWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private List<Chat> chat = new ArrayList<>();
  @Builder.Default
  @JsonIgnore
  private Map<String, SessionWrapper> sessions = new HashMap<>();

  public void broadcastEvent(AbstractBaseEvent event) {
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
