package dev.seanboaden.hls.media.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.media.model.Media;

public interface MediaRepository extends JpaRepository<Media, String> {
  Optional<Media> findByPath(String path);

  List<Media> findByTvSeasonIsNull();

  List<Media> findByMetadataIsNullOrInfoIsNull();
}
