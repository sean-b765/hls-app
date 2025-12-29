package dev.amethyst.app.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.amethyst.app.logging.Logging;
import dev.amethyst.app.room.event.RoomJoinedEvent;
import dev.amethyst.app.room.exception.RoomNotFoundException;
import dev.amethyst.app.room.model.Room;
import dev.amethyst.app.session.service.SessionWrapper;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomManager {
  private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
  private final Map<String, String> sessionRoomIds = new HashMap<>();

  public void printState() {
    ObjectMapper mapper = new ObjectMapper();
    StringBuilder builder = new StringBuilder().append("\nRoomManager state:");
    try {
      rooms.entrySet().forEach(entry -> {
        builder.append("\n\nRoom code: " + entry.getValue().getCode());
        builder.append("\nSessionIds: ");
        entry.getValue()
            .getSessions()
            .keySet()
            .forEach(session -> {
              builder.append(session + ", ");
            });
      });
      builder.append("\n\nSESSION MAP:\n");
      builder.append(mapper.writeValueAsString(sessionRoomIds));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Logging.info(builder.toString());
  }

  /**
   * @param name
   * @return the room
   */
  public Room create(String name) {
    Room room = Room.builder()
        .name(name)
        .code(generateRoomCode())
        .build();

    this.rooms.put(room.getCode(), room);
    return room;
  }

  public RoomJoinedEvent join(SessionWrapper session, String roomCode) throws RoomNotFoundException {
    if (!doesRoomExist(roomCode))
      throw new RoomNotFoundException();
    Room room = this.findRoomByCode(roomCode);
    if (room == null)
      throw new RoomNotFoundException();

    room.join(session);
    this.sessionRoomIds.put(session.getId(), roomCode);

    return RoomJoinedEvent.builder().room(room).build();
  }

  public void leave(SessionWrapper session) {
    if (!isSessionInARoom(session))
      return;
    String roomCode = findRoomCodeBySession(session);
    this.sessionRoomIds.remove(session.getId());

    if (!doesRoomExist(roomCode))
      return;
    Room room = this.findRoomByCode(roomCode);
    if (room == null)
      return;
    room.leave(session);
    if (room.getSessions().isEmpty()) {
      // Delete room if no one is left in it
      rooms.remove(room.getCode());
    }
  }

  public boolean isSessionInThisRoom(SessionWrapper session, String roomCode) {
    if (!this.sessionRoomIds.containsKey(session.getId()))
      return false;
    return this.sessionRoomIds.get(session.getId()).equals(roomCode);
  }

  public boolean isSessionInARoom(SessionWrapper session) {
    return this.sessionRoomIds.containsKey(session.getId());
  }

  public String findRoomCodeBySession(SessionWrapper session) {
    return this.sessionRoomIds.get(session.getId());
  }

  public @Nullable Room findRoomBySession(SessionWrapper session) {
    String roomCode = this.findRoomCodeBySession(session);
    return this.rooms.get(roomCode);
  }

  public @Nullable Room findRoomByCode(String roomCode) {
    return this.rooms.get(roomCode);
  }

  /**
   * @param roomCode
   * @return <code>true</code> if the room exists
   */
  private boolean doesRoomExist(String roomCode) {
    if (roomCode == null)
      return false;
    return this.rooms.containsKey(roomCode);
  }

  /**
   * @return a unique room code
   */
  private String generateRoomCode() {
    String roomCode;
    do {
      roomCode = this.generateCode();
    } while (doesRoomExist(roomCode));

    return roomCode;
  }

  /**
   * @return a random code like "XT53A2"
   */
  private String generateCode() {
    String letters = RandomStringUtils.random(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    String digits = RandomStringUtils.random(3, "123456789");
    String combined = letters + digits;

    List<Character> chars = new ArrayList<>();
    for (char c : combined.toCharArray())
      chars.add(c);
    Collections.shuffle(chars);

    StringBuilder sb = new StringBuilder();
    for (char c : chars)
      sb.append(c);

    return sb.toString();
  }
}
