package dev.seanboaden.hls.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledMediaScanService {
  @Autowired
  private MediaScanService mediaScanService;

  /**
   * Every 1 minute, scan the media root for files
   */
  @Scheduled(cron = "*/5 */1 * * * *")
  public void doScan() {
    mediaScanService.doScan();
  }
}
