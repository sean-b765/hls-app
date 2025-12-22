package dev.seanboaden.hls.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.configuration.model.Configuration;
import dev.seanboaden.hls.configuration.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
  @Autowired
  private ConfigurationRepository configurationRepository;

  public Configuration save(@NonNull Configuration configuration) {
    return this.configurationRepository.save(configuration);
  }

  public Configuration getConfiguration() {
    return this.configurationRepository.findById(1L).orElse(Configuration.builder().build());
  }
}
