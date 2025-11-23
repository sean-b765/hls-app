package dev.seanboaden.hls.video;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

public class QualityProfiles {
    public static final QualityProfile QHD1440P = QualityProfile.builder()
            .name("QHD1440P")
            .width(2560)
            .height(1440)
            .bandwidthUpperBound(12_000_000)
            .encodingBitRate(8_000_000)
            .build();
    public static final QualityProfile FHD1080P = QualityProfile.builder()
            .name("FHD1080P")
            .width(1920)
            .height(1080)
            .bandwidthUpperBound(5_500_000)
            .encodingBitRate(4_000_000)
            .build();
    public static final QualityProfile HD720P = QualityProfile.builder()
            .name("HD720P")
            .width(1280)
            .height(720)
            .bandwidthUpperBound(3_000_000)
            .encodingBitRate(2_000_000)
            .build();
    public static final QualityProfile SD480P = QualityProfile.builder()
            .name("SD480P")
            .width(854)
            .height(480)
            .bandwidthUpperBound(1_500_000)
            .encodingBitRate(1_000_000)
            .build();
    public static final QualityProfile SD360P = QualityProfile.builder()
            .name("SD360P")
            .width(640)
            .height(360)
            .bandwidthUpperBound(800_000)
            .encodingBitRate(600_000)
            .build();

    public static QualityProfile[] AllProfiles = new QualityProfile[]{
            QHD1440P,
            FHD1080P,
            HD720P,
            SD480P,
            SD360P
    };

    public static int getWidth(String qualityProfile) {
        return Integer.parseInt(qualityProfile.toLowerCase().split("x")[0]);
    }

    public static int getHeight(String qualityProfile) {
        return Integer.parseInt(qualityProfile.toLowerCase().split("x")[1]);
    }

    public static QualityProfile findByName(String qualityProfile) {
        return Arrays.stream(AllProfiles).filter(profile -> profile.getName().equals(qualityProfile)).findFirst().orElse(null);
    }

    @Builder
    @Data
    public static class QualityProfile {
        private String name;
        private int width;
        private int height;
        private int bandwidthUpperBound;
        private int encodingBitRate;

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }
}
