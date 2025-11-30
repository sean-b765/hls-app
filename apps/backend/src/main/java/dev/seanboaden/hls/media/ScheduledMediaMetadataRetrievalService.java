package dev.seanboaden.hls.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ScheduledMediaMetadataRetrievalService {
  @Autowired
  private MediaMetadataService mediaMetadataService;

  /**
   * Every hour, ensure metadata is up-to-date for each Media
   */
  @Scheduled(cron = "0 0 */1 * * *")
  public void doScan() {
    mediaMetadataService.getMetadata();
  }

  /**
   * Every week
   * Retrieve media info
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void scanForMediaInfo() {
    System.out.println("Scan for info");
  }
}
