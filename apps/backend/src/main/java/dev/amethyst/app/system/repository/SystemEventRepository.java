package dev.amethyst.app.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.system.model.SystemEvent;

public interface SystemEventRepository extends JpaRepository<SystemEvent, String> {

}
