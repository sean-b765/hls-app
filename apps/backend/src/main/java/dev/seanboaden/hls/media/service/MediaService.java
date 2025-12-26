package dev.seanboaden.hls.media.service;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.repository.MediaRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService extends AbstractCrudService<Media, String, MediaRepository> {
  protected MediaService(MediaRepository repository) {
    super(repository);
  }

  public Optional<Media> findByPath(String absolutePath) {
    return this.repository.findByPath(absolutePath);
  }

  public List<Media> findAllWhereTvSeasonIsNull() {
    return this.repository.findByTvSeasonIsNull();
  }

  public List<Media> findAllWhereMetadataIsNullOrInfoIsNull() {
    return this.repository.findByMetadataIsNullOrInfoIsNull();
  }

  public boolean existsByPath(String path) {
    return this.findByPath(path).isPresent();
  }

  public Media buildFromFile(File file) {
    return Media.builder().path(file.getAbsolutePath()).build();
  }
}
