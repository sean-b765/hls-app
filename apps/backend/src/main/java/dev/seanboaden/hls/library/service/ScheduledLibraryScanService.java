package dev.seanboaden.hls.library.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledLibraryScanService {
  @Autowired
  private LibraryScanService libraryScanService;
  @Autowired
  private LibraryService libraryService;

  /**
   * Scheduled scan each library for new files
   */
  @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
  public void doScan() {
    this.libraryScanService.ensureNewMedia();
  }
}
