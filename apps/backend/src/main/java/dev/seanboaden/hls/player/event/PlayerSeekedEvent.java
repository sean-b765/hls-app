package dev.seanboaden.hls.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayerSeekedEvent extends AbstractPlayerEvent {
  private Long timestampSeconds;
}
