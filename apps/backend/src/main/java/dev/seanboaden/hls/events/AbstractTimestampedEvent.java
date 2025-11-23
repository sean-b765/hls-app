package dev.seanboaden.hls.events;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractTimestampedEvent {
    @Builder.Default
    private String timestamp = Instant.now().toString();
}
