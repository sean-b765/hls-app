package dev.seanboaden.hls.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.video.QualityProfiles;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaylistManager {
  private final double segmentLength = 4.0;
  @Autowired
  private KeyframeExtractor keyframeExtractor;

  public String generateMultiVariantPlaylist(Media media) {
    StringBuilder multiVariantPlaylist = new StringBuilder();
    multiVariantPlaylist.append("#EXTM3U\n");
    for (QualityProfiles.QualityProfile profile : QualityProfiles.AllProfiles) {
      multiVariantPlaylist.append("#EXT-X-STREAM-INF:BANDWIDTH=")
          .append(profile.getBandwidthUpperBound())
          .append(",RESOLUTION=")
          .append(profile)
          .append("\n");
      multiVariantPlaylist.append("/api/playlist/")
          .append(media.getId())
          .append("/")
          .append(profile.getName())
          .append("\n");
    }
    return multiVariantPlaylist.toString();
  }

  public String generateVodPlaylist(Media media, QualityProfiles.QualityProfile qualityProfile) {
    KeyframeData keyframeData = keyframeExtractor.getKeyframeData(media.getPath());
    List<Double> segments = this.getSegments(keyframeData);

    StringBuilder vodPlaylist = new StringBuilder();
    vodPlaylist.append("#EXTM3U\n");
    vodPlaylist.append("#EXT-X-VERSION:3\n");
    vodPlaylist.append("#EXT-X-TARGETDURATION:").append(5).append("\n");
    vodPlaylist.append("#EXT-X-MEDIA-SEQUENCE:0\n");
    vodPlaylist.append("#EXT-X-PLAYLIST-TYPE:VOD\n");
    for (int i = 0; i < segments.size(); i++) {
      double segment = segments.get(i);
      vodPlaylist.append("#EXTINF:").append(segment).append("\n");
      String uri = String.format(
          "/api/video/%s/%s/segment%05d.ts",
          media.getId(),
          qualityProfile.getName(),
          i);
      vodPlaylist.append(uri).append("\n");
    }
    vodPlaylist.append("#EXT-X-ENDLIST");
    return vodPlaylist.toString();
  }

  public List<Double> getSegments(KeyframeData keyframeData) {
    List<Double> keyframes = keyframeData.getPositions();
    List<Double> segments = new ArrayList<>();

    if (keyframes.isEmpty()) {
      throw new IllegalArgumentException("Invalid KeyframeData passed to getSegments()");
    }

    double lastKeyframe = 0.0;
    double desiredCutTime = segmentLength;
    for (double keyframe : keyframes) {
      if (keyframe < desiredCutTime)
        continue;
      double currentSegmentLength = keyframe - lastKeyframe;
      segments.add(currentSegmentLength);
      lastKeyframe = keyframe;
      desiredCutTime += segmentLength;
    }

    return segments;
  }
}
