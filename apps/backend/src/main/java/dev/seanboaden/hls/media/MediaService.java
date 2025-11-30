package dev.seanboaden.hls.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MediaService {
  @Autowired
  private MediaRepository mediaRepository;
  @Value("${media.root}")
  private String rootPath;

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

  public Path getMediaRootPath() {
    if (this.rootPath == null)
      return null;
    return Paths.get(this.rootPath);
  }

  private List<Path> listFiles(Path path) {
    try (Stream<Path> paths = Files.walk(path)) {
      List<Path> foundPaths = paths.filter(Files::isRegularFile)
          .collect(Collectors.toList());
      return foundPaths;
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  public List<Path> listFilesRoot() {
    Path root = this.getMediaRootPath();
    if (root == null)
      return new ArrayList<>();
    return this.listFiles(root);
  }
}
