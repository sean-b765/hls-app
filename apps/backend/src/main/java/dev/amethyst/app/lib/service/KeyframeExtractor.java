package dev.amethyst.app.lib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.amethyst.app.playlist.model.KeyframeData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class KeyframeExtractor {
  @Autowired
  private FfmpegService ffmpegProcessHelper;

  public KeyframeData getKeyframeData(String inputFile) {
    // Build the ffprobe command
    Process process = this.ffmpegProcessHelper.ffprobe(
        "-fflags",
        "+genpts",
        "-v", "quiet",
        "-skip_frame", "nokey",
        "-select_streams", "v:0",
        "-show_entries", "format=duration",
        "-show_entries", "packet=pts_time",
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

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("keyframeout.json"))) {
      writer.write(root.toPrettyString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    KeyframeData keyframeData = new KeyframeData();
    double duration = root.path("format").path("duration").asDouble();
    keyframeData.setDuration(duration);

    JsonNode packets = root.path("packets");
    for (JsonNode pkt : packets) {
      double pts = pkt.path("pts_time").asDouble();
      keyframeData.getPositions().add(pts);
    }

    return keyframeData;
  }
}
