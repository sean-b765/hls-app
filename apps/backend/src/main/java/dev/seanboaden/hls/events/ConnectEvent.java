package dev.seanboaden.hls.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ConnectEvent extends AbstractWebSocketEvent {
}
