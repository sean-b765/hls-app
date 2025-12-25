package dev.seanboaden.hls.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledMediaScanService {
  @Autowired
  private MediaScanService mediaScanService;

  /**
   * Cron job to scan each library for new files
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void doScan() {
    // mediaScanService.doScan();
  }
}
