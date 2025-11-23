package dev.seanboaden.hls.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.chat.events.ChatEvent;
import dev.seanboaden.hls.room.Room;
import dev.seanboaden.hls.room.RoomManager;

@Component
public class ChatEventHandler {
    @Autowired
    private RoomManager roomManager;

    @EventListener
    private void handleChatEvent(ChatEvent event) {
        Room room = roomManager.findRoomBySession(event.getSession());
        event.getChat().setUserId(event.getSession().getUserId());
        room.getChat().add(event.getChat());
        room.broadcastEvent(event);
    }
}
