package dev.amethyst.app.metadata.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.amethyst.app.lib.service.FfmpegService;
import dev.amethyst.app.metadata.model.Metadata;

@Service
public class MetadataExtractor {
  @Autowired
  private FfmpegService ffmpegProcessHelper;

  public Metadata getMetadata(String absolutePath) {
    Process process = ffmpegProcessHelper.ffprobe(
        "-v", "quiet",
        "-show_entries",
        "stream=r_frame_rate,codec_name,codec_type,level,pix_fmt,bits_per_raw_sample,bit_rate,width,height,sample_rate,channels,channel_layout",
        "-show_entries", "format=duration,bit_rate,format_name,format_long_name",
        "-of", "json",
        absolutePath);
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
    try {
      return mapper.readValue(output.toString(), Metadata.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

}
