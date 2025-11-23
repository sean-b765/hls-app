package dev.seanboaden.hls.chat.events;

import dev.seanboaden.hls.chat.Chat;
import dev.seanboaden.hls.events.AbstractBaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChatEvent extends AbstractBaseEvent {
  private Chat chat;
}
