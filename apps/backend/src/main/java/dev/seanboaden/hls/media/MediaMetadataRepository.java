package dev.seanboaden.hls.media;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaMetadataRepository extends JpaRepository<MediaMetadata, String> {
  public Optional<MediaMetadata> findByMedia_Id(String mediaId);
}
