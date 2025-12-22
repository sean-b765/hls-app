package dev.seanboaden.hls.lib.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
public class MetadataExtractor {
  @Autowired
  private FfmpegService ffmpegProcessHelper;

  @Data
  public static class FrameRateAndDuration {
    private double framerate;
    private double duration;
  }

  public double getDurationSeconds(String absolutePath) {
    Process process = this.ffmpegProcessHelper.ffprobe(
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

  public double getFrameRate(String inputFile) {
    Process process = ffmpegProcessHelper.ffprobe(
        "-v", "quiet",
        "-select_streams", "v:0",
        "-of", "default=noprint_wrappers=1:nokey=1",
        "-show_entries", "stream=r_frame_rate",
        inputFile);
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
      String[] parts = output.split("/");
      return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0.0;
    }
  }

  public FrameRateAndDuration getFrameRateAndDuration(String inputFile) {
    Process process = ffmpegProcessHelper.ffprobe(
        "-v", "quiet",
        "-select_streams", "v:0",
        "-show_entries", "stream=r_frame_rate",
        "-show_entries", "format=duration",
        "-of", "json",
        inputFile);
    if (process == null)
      return null;

    // Read ffprobe JSON output
    StringBuilder output = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }
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

    // Parse JSON
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = null;
    try {
      root = mapper.readTree(output.toString());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    try {
      FrameRateAndDuration frameRateAndDuration = new FrameRateAndDuration();
      String framerateString = root.path("streams").path(0).path("r_frame_rate").asText();
      String[] parts = framerateString.split("/");

      double framerate = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
      double duration = root.path("format").path("duration").asDouble();

      frameRateAndDuration.setDuration(duration);
      frameRateAndDuration.setFramerate(framerate);
      return frameRateAndDuration;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return null;
    }
  }

}
