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
    return mediaRepository.save(media);
  }

  public Optional<Media> findByPath(String absolutePath) {
    return mediaRepository.findByPath(absolutePath);
  }

  public List<Media> findAll() {
    return mediaRepository.findAll();
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
