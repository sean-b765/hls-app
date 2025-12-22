package dev.seanboaden.hls.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.configuration.model.Configuration;

@Service
public class ConfigurationStartupService {
  @Value("${media.root}")
  private String mediaDirectory;
  @Value("${transcode.directory}")
  private String transcodeDirectory;

  @Autowired
  private ConfigurationService configurationService;

  @EventListener(ApplicationReadyEvent.class)
  public void createInitialConfig() {
    Configuration configuration = this.configurationService.getConfiguration();
    boolean isMediaDirBlank = configuration.getMediaDirectory() == null || configuration.getMediaDirectory().isEmpty();
    boolean isTranscodeDirBlank = configuration.getTranscodeDirectory() == null
        || configuration.getTranscodeDirectory().isEmpty();

    if (!isMediaDirBlank && !isTranscodeDirBlank)
      return;

    if (isMediaDirBlank) {
      configuration.setMediaDirectory(mediaDirectory);
    }

    if (isTranscodeDirBlank) {
      configuration.setTranscodeDirectory(transcodeDirectory);
    }

    configurationService.save(configuration);
  }
}
