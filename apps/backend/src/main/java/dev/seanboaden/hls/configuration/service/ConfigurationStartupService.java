package dev.seanboaden.hls.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.configuration.model.Configuration;

@Service
public class ConfigurationStartupService {
  @Value("${configuration.tmdb.apikey}")
  private String tmdbApiKey;
  @Value("${configuration.media.directory}")
  private String mediaDirectory;
  @Value("${configuration.transcode.directory}")
  private String transcodeDirectory;

  @Autowired
  private ConfigurationService configurationService;

  @EventListener(ApplicationReadyEvent.class)
  public void createInitialConfig() {
    Configuration configuration = this.configurationService.getConfiguration();
    boolean isMediaDirBlank = configuration.getMediaDirectory() == null || configuration.getMediaDirectory().isEmpty();
    boolean isTranscodeDirBlank = configuration.getTranscodeDirectory() == null
        || configuration.getTranscodeDirectory().isEmpty();
    boolean isTmdbApiKeyBlank = configuration.getTmdbApiKey() == null
        || configuration.getTmdbApiKey().isEmpty();

    if (!isMediaDirBlank && !isTranscodeDirBlank && !isTmdbApiKeyBlank)
      return;

    if (isMediaDirBlank) {
      configuration.setMediaDirectory(mediaDirectory);
    }

    if (isTranscodeDirBlank) {
      configuration.setTranscodeDirectory(transcodeDirectory);
    }

    if (isTmdbApiKeyBlank) {
      configuration.setTmdbApiKey(tmdbApiKey);
    }

    configurationService.save(configuration);
  }
}
