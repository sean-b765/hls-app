package dev.seanboaden.hls.library.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.web.AsyncModifier;
import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaScanService;
import dev.seanboaden.hls.media.service.MediaService;

@Service
public class LibraryScanService {
  @Autowired
  private FileSystemService fileSystemService;
  @Autowired
  private MediaScanService mediaScanService;
  @Autowired
  private MediaService mediaService;
  @Autowired
  private LibraryService libraryService;

  @Async(AsyncModifier.Modifier.SQLITE)
  public void ensureNewMedia() {
    this.libraryService.findAll().forEach(library -> {
      this.createNewMediaInLibrary(library);
    });
  }

  /**
   * Scan the library and creates Media for each new file
   */
  public List<Media> createNewMediaInLibrary(Library library) {
    Path path = this.fileSystemService.getLibraryPath(library);

    List<Media> newMedia = this.mediaScanService.findMissingMediaInPath(path).stream()
        .filter(media -> {
          // Ensure we only create Media if not existing
          return !this.mediaService.existsByPath(media.getPath());
        })
        .map(media -> {
          media.setLibrary(library);
          return media;
        })
        .collect(Collectors.toList());
    List<Media> savedMedia = this.mediaService.saveAll(newMedia);

    return savedMedia;
  }
}
