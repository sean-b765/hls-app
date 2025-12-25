package dev.seanboaden.hls.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.system.model.SystemEvent;

public interface SystemEventRepository extends JpaRepository<SystemEvent, String> {

}
