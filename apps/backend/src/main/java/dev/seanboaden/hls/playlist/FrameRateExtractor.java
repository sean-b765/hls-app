package dev.seanboaden.hls.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.FfmpegService;

@Service
public class FrameRateExtractor {
  @Autowired
  private FfmpegService ffmpegProcessHelper;

  public int getFrameRate(String inputFile) {
    Process process = ffmpegProcessHelper.ffprobe(
        "-v", "error",
        "-select_streams", "v",
        "-of", "default=noprint_wrappers=1:nokey=1",
        "-show_entries", "stream=r_frame_rate",
        inputFile);
    return 30;
  }

}
