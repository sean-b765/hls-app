package dev.seanboaden.hls.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.events.DisconnectEvent;
import dev.seanboaden.hls.media.MediaService;
import dev.seanboaden.hls.room.events.RoomCreateEvent;
import dev.seanboaden.hls.room.events.RoomJoinEvent;
import dev.seanboaden.hls.room.events.RoomJoinedEvent;
import dev.seanboaden.hls.room.events.RoomLeaveEvent;
import dev.seanboaden.hls.room.exception.RoomNotFoundException;

@Component
public class RoomEventHandler {
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private MediaService mediaService;

    @EventListener
    private void handleRoomJoinEvent(RoomJoinEvent event) {
        if (roomManager.isSessionInARoom(event.getSession())) {
            event.getSession().sendError("Already in a room. Please leave first.");
            return;
        }

        try {
            RoomJoinedEvent roomJoinedEvent = roomManager.join(event.getSession(), event.getRoomCode());
            event.getSession().sendEvent(roomJoinedEvent);
        } catch (RoomNotFoundException e) {
            event.getSession().sendError("The room was not found.");
        }
    }

    @EventListener
    private void handleRoomCreateEvent(RoomCreateEvent event) {
        if (event.getRoomName().isEmpty()) {
            event.getSession().sendError("Please provide a room name.");
            return;
        }
        if (roomManager.isSessionInARoom(event.getSession())) {
            event.getSession().sendError("Already in a room. Please leave first.");
            return;
        }

        Room room = roomManager.create(event.getRoomName());
        try {
            RoomJoinedEvent roomJoinedEvent = roomManager.join(event.getSession(), room.getCode());
            event.getSession().sendEvent(roomJoinedEvent);
            event.getSession().sendMessage(mediaService.listFilesRoot());
        } catch (RoomNotFoundException e) {
            event.getSession().sendError("Room not found after creating.");
        }
    }

    @EventListener
    private void handleRoomLeaveEvent(RoomLeaveEvent event) {
        roomManager.leave(event.getSession());
        String roomCode = roomManager.findRoomCodeBySession(event.getSession());
        Room room = roomManager.findRoomByCode(roomCode);
        if (room == null) return;

        room.broadcastEvent(event);
    }

    @EventListener
    private void handleDisconnect(DisconnectEvent event) {
        roomManager.leave(event.getSession());
        roomManager.printState();
    }

}
