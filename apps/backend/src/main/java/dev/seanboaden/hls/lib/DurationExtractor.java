package dev.seanboaden.hls.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DurationExtractor {
  @Autowired
  private FfmpegService ffmpegService;

  public double getDurationSeconds(String absolutePath) {
    Process process = this.ffmpegService.ffprobe(
        "-v", "quiet",
        "-select_streams", "v:0",
        "-show_entries", "format=duration",
        "-of", "default=noprint_wrappers=1:nokey=1",
        absolutePath);

    if (process == null)
      return 0.0;

    // Read ffprobe output
    String output = new String();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      output = reader.lines().collect(Collectors.joining()).trim();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    int exitCode = 0;
    try {
      exitCode = process.waitFor();
    } catch (InterruptedException e) {
      // ffprobe failed
      throw new RuntimeException(e);
    }

    if (exitCode != 0) {
      throw new RuntimeException("ffprobe exited with code " + exitCode);
    }

    try {
      return Double.parseDouble(output.toString());
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}
