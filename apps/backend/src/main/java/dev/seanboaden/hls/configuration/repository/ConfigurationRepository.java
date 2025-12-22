package dev.seanboaden.hls.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.configuration.model.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
