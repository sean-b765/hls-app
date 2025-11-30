package dev.seanboaden.hls.media;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaInfoRepository extends JpaRepository<MediaInfo, String> {
  public Optional<MediaInfo> findByMedia_Id(String mediaId);
}