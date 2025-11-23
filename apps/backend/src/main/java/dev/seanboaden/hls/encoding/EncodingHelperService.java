package dev.seanboaden.hls.encoding;

import org.springframework.stereotype.Service;

@Service
public class EncodingHelperService {
  /*
   * ffmpeg -i "input.mkv" -preset superfast -map 0:v -map 0:a -codec:v libx264
   * -profile:v high
   * -level:v 4.0 -b:v 1000k (bitrate) -codec:a aac -ac 2 -b:a 128k -f hls
   * -hls_time 4 (segment
   * length) -hls_playlist_type vod -hls_segment_filename "segment%05d.ts"
   * -hls_segment_type mpegts
   * -pix_fmt yuv420p (this is required for hls.js and many browsers)
   * -force_key_frames
   * "expr:gte(t,n_forced*4)" -g:v:0 96 (set groups of pictures to framerate *
   * segment length = 4)
   * -keyint_min:v:0 96 -x264opts:0
   * "no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0"
   * -sc_threshold:v:0 0 -bf 0
   * -r 23.97598 (frame rate of source file) -fps_mode cfr (use constant frame
   * rate) master.m3u8
   */
  private final String baseArgs = "-preset superfast -bf 0 -r %s -fps_mode cfr -sc_threshold:v:0 0";
  private final String hlsArgs = "-f hls -hls_time 4 -hls_playlist_type vod -hls_segment_filename \"segment%05d.ts\" -hls_segment_type mpegts";
  private final String x264opts = "-x264opts:0 \"no-scenecut=1:subme=0:me_range=16:rc_lookahead=0:me=hex:open_gop=0\"";

  // public String getArgs() {
  //
  // }
}
