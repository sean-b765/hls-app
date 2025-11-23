package dev.seanboaden.hls.playlist;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * A keyframe is the complete [I] frame of video which marks the starting/end point of a group of pictures
 */
@Data
@NoArgsConstructor
public class KeyframeData {
    /**
     * Total duration of video
     */
    private double duration;
    /**
     * List of keyframe's positions
     */
    private List<Double> positions = new ArrayList<>();
}
