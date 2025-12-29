package dev.amethyst.app.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.configuration.model.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
