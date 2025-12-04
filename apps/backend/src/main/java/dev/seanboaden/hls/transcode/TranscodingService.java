package dev.seanboaden.hls.transcode;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.MimeTypeService;
import dev.seanboaden.hls.media.Media;

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
   * -r 23.97598
   * ^^^ frame rate of source file
   * -fps_mode cfr
   * ^^^ use constant frame rate
   * master.m3u8
   * ^^^ output playlist file
   */
  private final Pattern segmentPattern = Pattern.compile("segment(\\d+)\\.ts");
  private final long segmentLength = 4;
  private final long segmentCountLimit = 8;

  private final String inputArg = "-i \"%s\" ";
  private final String baseVideoArgs = "-preset superfast -bf 0 -fps_mode cfr -sc_threshold:v:0 0 -pix_fmt yuv420p -r %s ";
  private final String hlsArgs = "-f hls -hls_time " +
      String.valueOf(this.segmentLength)
      + " -hls_playlist_type vod -hls_segment_filename \"segment%05d.ts\" -hls_segment_type mpegts ";
  private final String x264Args = "-x264opts:0 \"no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0\" ";
  private final String gopArgs = "-force_key_frames \"expr:gte(t,n_forced*" +
      String.valueOf(this.segmentLength) + ")\" -g:v:0 %s -keyint_min:v:0 %s ";
  // 8 segments at a time
  private final String transcodeCeilingArgs = "-t " + String.valueOf(this.segmentLength * this.segmentCountLimit) + " ";
  private final String startNumberArg = "-start_number %s -ss %s ";

  @Autowired
  private MimeTypeService mimeTypeService;

  private String getVideoArgs(Media media) {
    StringBuilder argsBuilder = new StringBuilder();
    argsBuilder.append(String.format(baseVideoArgs, media.getMetadata().getFramerate()));
    double gop = media.getMetadata().getFramerate() * this.segmentLength;
    argsBuilder.append(String.format(gopArgs, gop, gop));

    if ("x264".equals("x264")) {
      argsBuilder.append(x264Args);
    }

    return argsBuilder.toString();
  }

  public String[] getHlsArgs(TranscodeJob transcodeJob, String outputDirectory) {
    Media media = transcodeJob.getMedia();
    StringBuilder argsBuilder = new StringBuilder();

    argsBuilder.append(String.format(inputArg, transcodeJob.getMedia().getPath()));

    if (mimeTypeService.isMusicType(media.getPath())) {
      // handle music decode args
    } else if (mimeTypeService.isVideoType(media.getPath())) {
      argsBuilder.append(this.getVideoArgs(media));
    }

    argsBuilder.append(hlsArgs);
    argsBuilder.append(transcodeCeilingArgs);
    long fromSegmentNumber = this.extractSegmentNumber(transcodeJob.getFromSegmentName());
    double fromTimestampSeconds = fromSegmentNumber * this.segmentLength;

    argsBuilder.append(String.format(this.startNumberArg, fromSegmentNumber, fromTimestampSeconds));
    argsBuilder.append(String.format("\"%s\"", StringUtils.joinWith("/", outputDirectory, "index.m3u8")));

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

  /**
   * Find the next segment number that already exists in the output directory.
   * 
   * @return the segment number of the next segment, or -1 if no next segment
   */
  public long findNextExistingSegment(String outputDirectory, long startFrom) {
    long startingPoint = startFrom;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(outputDirectory))) {
      for (Path path : stream) {
        if (!Files.isRegularFile(path))
          continue;
        Matcher matcher = this.segmentPattern.matcher(path.getFileName().toString());
        if (matcher.find()) {
          long segmentNumber = Long.parseLong(matcher.group(1));
          if (segmentNumber > startingPoint) {
            startingPoint = segmentNumber;
          }
        }
      }
    } catch (IOException e) {
      // Directory might not exist yet, return -1
      return -1;
    }
    return startingPoint;
  }

  /**
   * This will determine how many segments to transcode from the requested point.
   * 
   * @return the number of segments to transcode, or the default segmentCountLimit
   */
  public long calculateSegmentRunLength(long requestedSegment, String outputDirectory) {
    long nextExistingSegment = findNextExistingSegment(outputDirectory, requestedSegment);

    if (nextExistingSegment == -1) {
      return this.segmentCountLimit;
    }

    // If requested segment is beyond what already exists
    if (requestedSegment <= nextExistingSegment) {
      return 0L;
    }

    // Calculate how many segments to transcode to reach the next existing segment,
    // but cap it at the segmentCountLimit
    long segmentsToTranscode = nextExistingSegment - requestedSegment + 1;
    if (segmentsToTranscode > this.segmentCountLimit)
      return this.segmentCountLimit;
    return segmentsToTranscode;
  }

  public String toSegmentName(long segmentNum) {
    return String.format("segment%05d.ts", segmentNum);
  }

  public String toSegmentName(int segmentNum) {
    return String.format("segment%05d.ts", segmentNum);
  }
}
