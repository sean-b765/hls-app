package dev.seanboaden.hls.library.service;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.media.event.MediaCreatedEvent;
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
  private ApplicationEventPublisher eventPublisher;

  /**
   * Scan the library and creates Media for each new file
   */
  public List<Media> createNewMediaInLibrary(Library library) {
    Path path = this.fileSystemService.getLibraryPath(library);

    List<Media> newMedia = this.mediaScanService.listNewMediaInPath(path);
    List<Media> savedMedia = this.mediaService.saveAll(newMedia);

    /**
     * Dispatch a CreatedMediaEvent
     */
    savedMedia.forEach(media -> {
      MediaCreatedEvent event = MediaCreatedEvent.builder().media(media).build();
      eventPublisher.publishEvent(event);
    });

    return savedMedia;
  }
}
