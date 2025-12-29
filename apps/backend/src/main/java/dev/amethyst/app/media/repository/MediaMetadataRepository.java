package dev.amethyst.app.media.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.media.model.MediaMetadata;

public interface MediaMetadataRepository extends JpaRepository<MediaMetadata, String> {
  public Optional<MediaMetadata> findByMedia_Id(String mediaId);
}
