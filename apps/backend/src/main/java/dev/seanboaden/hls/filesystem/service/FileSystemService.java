package dev.seanboaden.hls.filesystem.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.configuration.service.ConfigurationService;
import dev.seanboaden.hls.filesystem.model.FolderNode;
import dev.seanboaden.hls.transcode.model.TranscodeJob;

@Service
public class FileSystemService {
  @Autowired
  private ConfigurationService configurationService;

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

  public LinkedList<FolderNode> listFolders(Path relativePath) {
    Path mediaRoot = Paths.get(this.configurationService.getConfiguration().getMediaDirectory());
    Path base = mediaRoot.resolve(relativePath).normalize();

    if (!base.startsWith(mediaRoot))
      return new LinkedList<>();

    try (Stream<Path> paths = Files.list(base)) {
      return paths
          .filter(Files::isDirectory)
          .sorted()
          .map(p -> FolderNode.builder()
              .name(p.getFileName().toString())
              .path(mediaRoot.relativize(p).toString().replace("\\", "/"))
              .hasChildren(this.hasSubdirectories(p))
              .build())
          .collect(Collectors.toCollection(LinkedList::new));
    } catch (IOException e) {
      return new LinkedList<>();
    }
  }

  private boolean hasSubdirectories(Path path) {
    try (Stream<Path> s = Files.list(path)) {
      return s.anyMatch(Files::isDirectory);
    } catch (IOException ex) {
      return false;
    }
  }
}
