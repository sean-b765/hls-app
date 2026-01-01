package dev.amethyst.app.transcode.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.metadata.model.PlaybackCompatibility;
import dev.amethyst.app.metadata.service.PlaybackHelperService;
import dev.amethyst.app.transcode.model.TranscodeJob;
import lombok.Getter;

@Service
public class TranscodingService {
  /*
   * ffmpeg -i "input.mkv" -preset superfast -map 0:v -map 0:a
   * -codec:v libx264
   * ^^^ maybe keep to this to ensure max compatibility - but h265 might be nicer
   * -profile:v high
   * -level:v 4.0
   * ^^^ this may need to be tweaked depending on codec
   * -b:v 1000k
   * ^^^ video bitrate
   * -codec:a aac
   * -ac 2
   * -b:a 128k
   * ^^^ audio bitrate
   * -f hls
   * -hls_time 4
   * ^^^ segment length
   * -hls_playlist_type vod
   * -hls_segment_filename "segment%05d.ts"
   * -hls_segment_type mpegts
   * -pix_fmt yuv420p
   * ^^^ this is required for hls.js and many browsers
   * -force_key_frames
   * "expr:gte(t,n_forced*4)" -g:v:0 96 -keyint_min:v:0 96
   * ^^^ set groups of pictures to framerate * segment length = 4
   * -x264opts:0
   * "no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0"
   * -sc_threshold:v:0 0 -bf 0
   * -r 24
   * ^^^ frame rate of source file
   * -fps_mode cfr
   * ^^^ use constant frame rate
   * master.m3u8
   * ^^^ output playlist file
   */
  private final Pattern segmentPattern = Pattern.compile("segment(\\d+)\\.ts");
  @Getter
  private final double segmentLength = 4.0;
  @Getter
  private final long segmentCountLimit = 8;

  private final String baseVideoArgs = "-preset superfast ";
  private final String constantFramerateArgs = "-bf 0 -fps_mode cfr -sc_threshold:v:0 0 -r %s ";
  private final String forcePixelFormatArgs = "-pix_fmt yuv420p ";
  private final String videoCodecArgs = "-codec:v libx264 ";
  private final String keepVideoCodecArgs = "-codec:v copy ";
  private final String audioCodecArgs = "-codec:a aac ";
  private final String keepAudioCodecArgs = "-codec:a copy ";
  private final String hlsMuxerArgs = "-f hls -hls_time " +
      String.valueOf(this.segmentLength)
      + " -hls_playlist_type vod -hls_segment_filename \"segment%05d.ts\" -hls_segment_type mpegts ";
  private final String inputArg = "-i \"%s\" ";
  private final String x264Args = "-x264opts:0 \"no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0\" ";
  private final String gopArgs = "-force_key_frames \"expr:gte(t,n_forced*" +
      String.valueOf(this.segmentLength) + ")\" -g:v:0 %s -keyint_min:v:0 %s ";
  private final String startNumberArg = "-start_number %s -ss %s ";

  @Autowired
  private PlaybackHelperService playbackHelperService;

  /**
   * @param media
   * @return ffmpeg arguments for the video file, transcoding only if necessary
   */
  private String getVideoArgs(Media media) {
    StringBuilder argsBuilder = new StringBuilder();
    if (media == null || media.getMetadata() == null)
      return argsBuilder.toString();

    // TODO: if possible, avoid at all cost
    long fps = media.getMetadata().getFramerate();
    argsBuilder.append(String.format(this.constantFramerateArgs, fps));

    // TODO: if possible, avoid at all cost
    double gop = fps * this.segmentLength;
    argsBuilder.append(String.format(this.gopArgs, gop, gop));

    // TODO: if possible, avoid at all cost
    argsBuilder.append(this.forcePixelFormatArgs);

    PlaybackCompatibility compatibility = this.playbackHelperService.getPlaybackCompatibility(media.getMetadata());
    switch (compatibility) {
      case TRANSCODE_BOTH:
        argsBuilder.append(this.baseVideoArgs);
        argsBuilder.append(this.videoCodecArgs);
        // argsBuilder.append(this.x264Args);
        argsBuilder.append(this.audioCodecArgs);
        break;
      case TRANSCODE_AUDIO:
        argsBuilder.append(this.keepVideoCodecArgs);
        argsBuilder.append(this.audioCodecArgs);
        break;
      case TRANSCODE_VIDEO:
        argsBuilder.append(this.keepAudioCodecArgs);
        argsBuilder.append(this.baseVideoArgs);
        argsBuilder.append(this.videoCodecArgs);
        // argsBuilder.append(this.x264Args);
        break;
    }

    return argsBuilder.toString();
  }

  /**
   * @param media
   * @return ffmpeg arguments for the audio file, only transcoding if necessary
   */
  private String getAudioArgs(Media media) {
    StringBuilder argsBuilder = new StringBuilder();
    if (media == null || media.getMetadata() == null)
      return argsBuilder.toString();

    PlaybackCompatibility compatibility = this.playbackHelperService.getPlaybackCompatibility(media.getMetadata());
    switch (compatibility) {
      case TRANSCODE_AUDIO:
        argsBuilder.append(this.audioCodecArgs);
        break;
    }

    return argsBuilder.toString();
  }

  public String[] getHlsArguments(TranscodeJob transcodeJob, String outputDirectory) {
    Media media = transcodeJob.getMedia();
    StringBuilder argsBuilder = new StringBuilder();

    // Input file args
    argsBuilder.append(String.format(this.inputArg, transcodeJob.getMedia().getPath()));

    // Get audio/video transcode args if necessary
    if (media.getMetadata().getContainer().isAudio()) {
      argsBuilder.append(this.getAudioArgs(media));
    } else if (media.getMetadata().getContainer().isVideo()) {
      argsBuilder.append(this.getVideoArgs(media));
    }

    // Use hls muxer always
    argsBuilder.append(this.hlsMuxerArgs);

    // Set any start offsets
    long fromSegmentNumber = this.extractSegmentNumber(transcodeJob.getFromSegmentName());
    double fromTimestampSeconds = fromSegmentNumber * this.segmentLength;
    argsBuilder.append(String.format(this.startNumberArg, fromSegmentNumber, fromTimestampSeconds));

    // Set output path
    String outputPath = String.format("\"%s\"", StringUtils.joinWith("/", outputDirectory, "index.m3u8"));
    argsBuilder.append(outputPath);

    return argsBuilder.toString().split(" ");
  }

  public long extractSegmentNumber(String filename) {
    Matcher matcher = segmentPattern
        .matcher(filename);

    if (matcher.find()) {
      return Long.parseLong(matcher.group(1));
    }

    throw new IllegalArgumentException("Invalid segment filename: " + filename);
  }

  public String toSegmentName(long segmentNum) {
    return String.format("segment%05d.ts", segmentNum);
  }

  public String toSegmentName(int segmentNum) {
    return String.format("segment%05d.ts", segmentNum);
  }
}
