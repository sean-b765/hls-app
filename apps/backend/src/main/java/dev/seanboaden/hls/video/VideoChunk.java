package dev.seanboaden.hls.video;

public class VideoChunk {
    private final long timestampMs;
    private final boolean keyframe;
    private final byte[] data;

    public VideoChunk(long timestampMs, boolean keyframe, byte[] data) {
        this.timestampMs = timestampMs;
        this.keyframe = keyframe;
        this.data = data;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public boolean isKeyframe() {
        return keyframe;
    }

    public byte[] getData() {
        return data;
    }
}

