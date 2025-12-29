package dev.amethyst.app.room.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.amethyst.app.room.command.RoomCreateCommand;
import dev.amethyst.app.room.command.RoomJoinCommand;
import dev.amethyst.app.room.command.RoomLeaveCommand;
import dev.amethyst.app.room.event.RoomJoinedEvent;
import dev.amethyst.app.room.exception.RoomNotFoundException;
import dev.amethyst.app.room.model.Room;
import dev.amethyst.app.room.service.RoomManager;
import dev.amethyst.app.user.event.DisconnectEvent;

@Component
public class RoomCommandHandler {
  @Autowired
  private RoomManager roomManager;

  @EventListener
  private void onRoomJoinCommand(RoomJoinCommand command) {
    if (roomManager.isSessionInARoom(command.getSession())) {
      command.getSession().sendError("Already in a room. Please leave first.");
      return;
    }

    try {
      RoomJoinedEvent roomJoinedEvent = roomManager.join(command.getSession(), command.getCode());
      command.getSession().sendEvent(roomJoinedEvent);
    } catch (RoomNotFoundException e) {
      command.getSession().sendError("The room was not found.");
    }
  }

  @EventListener
  private void onRoomCreateCommand(RoomCreateCommand command) {
    if (command.getName().isEmpty()) {
      command.getSession().sendError("Please provide a room name.");
      return;
    }
    if (roomManager.isSessionInARoom(command.getSession())) {
      command.getSession().sendError("Already in a room. Please leave first.");
      return;
    }

    Room room = roomManager.create(command.getName());
    try {
      RoomJoinedEvent roomJoinedEvent = roomManager.join(command.getSession(), room.getCode());
      command.getSession().sendEvent(roomJoinedEvent);
    } catch (RoomNotFoundException e) {
      command.getSession().sendError("Room not found.");
    }
  }

  @EventListener
  private void onRoomLeaveCommand(RoomLeaveCommand command) {
    roomManager.leave(command.getSession());
    String roomCode = roomManager.findRoomCodeBySession(command.getSession());
    Room room = roomManager.findRoomByCode(roomCode);
    if (room == null)
      return;

    room.broadcastEvent(command);
  }

  @EventListener
  private void onDisconnect(DisconnectEvent event) {
    roomManager.leave(event.getSession());
    roomManager.printState();
  }

}
