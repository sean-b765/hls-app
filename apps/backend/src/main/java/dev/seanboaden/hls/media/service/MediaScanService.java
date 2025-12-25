package dev.seanboaden.hls.media.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.lib.service.MimeTypeService;
import dev.seanboaden.hls.media.model.Media;

@Service
public class MediaScanService {
  @Autowired
  private MediaService mediaService;
  @Autowired
  private MimeTypeService mimeTypeService;
  @Autowired
  private FileSystemService fileSystemService;

  /**
   * Generate a list of Media which do not exist in database for the path
   * 
   * @param path the path to create new media for
   * @return
   */
  public List<Media> listNewMediaInPath(Path path) {
    List<Path> paths = this.fileSystemService.listFiles(path);

    return paths.stream()
        .map(p -> new File(p.toUri()))
        .filter(File::canRead)
        .filter(file -> this.mimeTypeService.isVideoType(file.getAbsolutePath())
            || this.mimeTypeService.isMusicType(file.getAbsolutePath()))
        .filter(file -> !this.mediaService.existsByPath(file.getAbsolutePath()))
        .map(this.mediaService::buildFromFile)
        .toList();
  }
}
