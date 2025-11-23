package dev.seanboaden.hls.session.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.seanboaden.hls.events.AbstractTimestampedEvent;
import dev.seanboaden.hls.logging.Logging;

import org.springframework.web.socket.TextMessage;

public abstract class ResponseConverter extends AbstractTimestampedEvent {
    @JsonIgnore
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TextMessage convert() {
        try {
            return new TextMessage(objectMapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            Logging.info("ERROR WHILE CONVERTING");
            return null;
        }
    }
}
