package dev.seanboaden.hls.encoding;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.MimeTypeService;
import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.video.TranscodeJob;

@Service
public class EncodingService {
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
  private final String inputArg = "-i \"%s\" ";
  private final String baseVideoArgs = "-preset superfast -bf 0 -fps_mode cfr -sc_threshold:v:0 0 -pix_fmt yuv420p -r %s ";
  private final String hlsArgs = "-f hls -hls_time 4 -hls_playlist_type vod -hls_segment_filename \"segment%05d.ts\" -hls_segment_type mpegts ";
  private final String x264Args = "-x264opts:0 \"no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0\" ";
  private final String gopArgs = "-force_key_frames \"expr:gte(t,n_forced*4)\" -g:v:0 %s -keyint_min:v:0 %s ";
  private final String transcodeCeilingArgs = "-t 80 ";

  @Autowired
  private MimeTypeService mimeTypeService;

  private String getVideoArgs(Media media) {
    StringBuilder argsBuilder = new StringBuilder();
    argsBuilder.append(String.format(baseVideoArgs, media.getMetadata().getFramerate()));

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
    argsBuilder.append(String.format("\"%s\"", StringUtils.joinWith("/", outputDirectory, "index.m3u8")));

    return argsBuilder.toString().split(" ");
  }
}
