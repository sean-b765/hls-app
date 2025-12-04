package dev.seanboaden.hls.lib;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.transcode.TranscodeJob;

@Service
public class FileSystemService {
  @Value("${transcode.directory}")
  private String transcodeDirectory;

  public String getSegmentDirectory(TranscodeJob transcodeJob) {
    return StringUtils.joinWith(
        "/",
        this.transcodeDirectory,
        transcodeJob.getType().name(),
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName());
  }
}
