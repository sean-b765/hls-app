package dev.seanboaden.hls.filesystem.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.configuration.service.ConfigurationService;
import dev.seanboaden.hls.filesystem.model.FolderNode;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.transcode.model.TranscodeJob;

@Service
public class FileSystemService {
  @Autowired
  private ConfigurationService configurationService;

  public Path getMediaRoot() {
    return Paths.get(configurationService.getConfiguration().getMediaDirectory())
        .toAbsolutePath()
        .normalize();
  }

  public String getSegmentDirectory(TranscodeJob transcodeJob) {
    return StringUtils.joinWith(
        "/",
        this.configurationService.getConfiguration().getTranscodeDirectory(),
        transcodeJob.getType().name(),
        transcodeJob.getMedia().getId(),
        transcodeJob.getQuality().getName());
  }

  public Path getSegmentPath(TranscodeJob transcodeJob) {
    String segmentPath = this.getSegmentDirectory(transcodeJob);
    return Paths.get(segmentPath, transcodeJob.getFromSegmentName());
  }

  public Path getLibraryPath(Library library) {
    if (library == null || library.getPath() == null)
      throw new IllegalArgumentException();

    Path path = this.resolve(this.getMediaRoot(), library.getPath());

    if (!this.isValidDirectory(path))
      throw new SecurityException();

    return path;
  }

  /**
   * Validates that the relative path does in fact exist under the media root,
   * and not escalated somehow
   * 
   * @param relativePath
   * @return
   */
  public boolean isValidDirectory(Path relativePath) {
    Path mediaRoot = this.getMediaRoot();
    Path resolved = this.resolve(mediaRoot, relativePath);
    if (!resolved.startsWith(mediaRoot))
      return false;
    if (!Files.exists(resolved))
      return false;
    if (!Files.isDirectory(resolved))
      return false;
    return true;
  }

  public LinkedList<FolderNode> listFoldersUnderMediaRoot(Path relativePath) {
    Path mediaRoot = this.getMediaRoot();
    Path base = mediaRoot.resolve(relativePath).normalize();

    if (!this.isValidDirectory(base))
      return new LinkedList<>();

    try (Stream<Path> paths = Files.list(base)) {
      return paths
          .filter(Files::isDirectory)
          .sorted()
          .map(p -> FolderNode.builder()
              .name(p.getFileName().toString())
              .path(this.normalizePath(mediaRoot.relativize(p)))
              .hasChildren(this.hasSubdirectories(p))
              .build())
          .collect(Collectors.toCollection(LinkedList::new));
    } catch (IOException e) {
      return new LinkedList<>();
    }
  }

  public List<Path> listFiles(Path path) {
    try (Stream<Path> paths = Files.walk(path)) {
      List<Path> foundPaths = paths.filter(Files::isRegularFile)
          .collect(Collectors.toList());
      return foundPaths;
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  private boolean hasSubdirectories(Path path) {
    try (Stream<Path> s = Files.list(path)) {
      return s.anyMatch(Files::isDirectory);
    } catch (IOException ex) {
      return false;
    }
  }

  private Path resolve(Path root, String relativePath) {
    return root.resolve(relativePath).normalize();
  }

  private Path resolve(Path root, Path relativePath) {
    return root.resolve(relativePath).normalize();
  }

  private String normalizePath(Path path) {
    return path.toString().replace("\\", "/");
  }
}
