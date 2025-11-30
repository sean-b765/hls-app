package dev.seanboaden.hls.media;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String> {
  Optional<Media> findByPath(String path);

  List<Media> findByTvSeasonIsNull();
}
