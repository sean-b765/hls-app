package dev.seanboaden.hls.media.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.media.model.MediaInfo;

public interface MediaInfoRepository extends JpaRepository<MediaInfo, String> {
  public Optional<MediaInfo> findByMediaId(String mediaId);
}