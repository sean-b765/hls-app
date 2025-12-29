package dev.amethyst.app.media.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.media.model.Media;

public interface MediaRepository extends JpaRepository<Media, String> {
  Optional<Media> findByPath(String path);

  List<Media> findByTvSeasonIsNull();

  List<Media> findByMetadataIsNullOrInfoIsNull();

  void deleteAllByLibrary_Id(String libraryId);
}
