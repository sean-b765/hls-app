package dev.amethyst.app.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dev.amethyst.app.configuration.model.Configuration;
import dev.amethyst.app.configuration.repository.ConfigurationRepository;

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
