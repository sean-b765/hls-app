package dev.seanboaden.hls.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.repository.MediaRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
  @Autowired
  private MediaRepository mediaRepository;

  public Media save(Media media) {
    if (media == null)
      return media;
    return this.mediaRepository.save(media);
  }

  public List<Media> saveAll(List<Media> media) {
    if (media == null)
      return new ArrayList<>();
    return this.mediaRepository.saveAll(media);
  }

  public Optional<Media> findByPath(String absolutePath) {
    return this.mediaRepository.findByPath(absolutePath);
  }

  public List<Media> findAll() {
    return this.mediaRepository.findAll();
  }

  public List<Media> findAllWhereTvSeasonIsNull() {
    return this.mediaRepository.findByTvSeasonIsNull();
  }

  public Optional<Media> findById(String id) {
    if (id == null)
      return Optional.empty();
    return this.mediaRepository.findById(id);
  }

  public boolean existsByPath(String path) {
    return this.findByPath(path).isPresent();
  }

  public Media buildFromFile(File file) {
    return Media.builder().path(file.getAbsolutePath()).build();
  }
}
