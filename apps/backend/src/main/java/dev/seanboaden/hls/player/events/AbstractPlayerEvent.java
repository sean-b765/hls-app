package dev.seanboaden.hls.player.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.seanboaden.hls.events.AbstractBaseEvent;
import dev.seanboaden.hls.events.BaseEventType;
import dev.seanboaden.hls.player.enums.PlayerEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChooseTrackEvent.class, name = PlayerEventType.CHOOSE_TRACK),
})
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractPlayerEvent extends AbstractBaseEvent {
    private String eventType;

    {
        setType(BaseEventType.PLAYER);
    }
}
