package dev.seanboaden.hls.lib;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FfmpegService {
  private final String ffprobePath = "ffprobe";
  private final String ffmpegPath = "ffmpeg";

  public Process ffprobe(String... arguments) {
    List<String> command = new ArrayList<>();
    command.add(ffprobePath);
    command.addAll(List.of(arguments));

    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectError(new File("ffprobe-error.txt"));

    try {
      return pb.start();
    } catch (IOException e) {
      return null;
    }
  }

  public Process ffmpeg(String workingDirectory, String... arguments) {
    List<String> command = new ArrayList<>();
    command.add(ffmpegPath);
    command.addAll(List.of(arguments));

    ProcessBuilder pb = new ProcessBuilder(command);

    File log = new File("ffmpeg-error.txt");
    pb.redirectError(log);
    pb.directory(new File(workingDirectory));

    try {
      return pb.start();
    } catch (IOException e) {
      return null;
    }
  }

}
