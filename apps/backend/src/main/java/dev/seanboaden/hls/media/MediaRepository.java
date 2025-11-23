package dev.seanboaden.hls.media;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
  Optional<Media> findByPath(String path);
}
