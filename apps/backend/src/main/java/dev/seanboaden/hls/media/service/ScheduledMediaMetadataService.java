package dev.seanboaden.hls.media.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Every minute, retrieve metadata and info for all necessary media
 */
@Service
public class ScheduledMediaMetadataService {
  @Autowired
  private MediaService mediaService;

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
  public void ensureMetadataAndInfo() {
    this.mediaService.findAllWhereMetadataIsNullOrInfoIsNull().forEach(media -> {
      if (media.getMetadata() == null)
        this.mediaService.ensureMetadata(media.getId());
      if (media.getInfo() == null)
        this.mediaService.ensureInfo(media.getId());
    });
  }
}
