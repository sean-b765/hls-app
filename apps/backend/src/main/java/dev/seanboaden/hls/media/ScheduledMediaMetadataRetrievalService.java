package dev.seanboaden.hls.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledMediaMetadataRetrievalService {
  @Autowired
  private MediaMetadataService mediaMetadataService;

  /**
   * Every 1 minute, ensure metadata retrieval for each Media row
   */
  @Scheduled(cron = "*/30 * * * * *")
  public void doScan() {
    mediaMetadataService.getMetadata();
  }
}
