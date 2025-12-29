package dev.amethyst.app.media.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.media.model.MediaInfo;

public interface MediaInfoRepository extends JpaRepository<MediaInfo, String> {
  public Optional<MediaInfo> findByMediaId(String mediaId);
}