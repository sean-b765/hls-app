package dev.seanboaden.hls.media;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.lib.MimeTypeService;

@Service
public class MediaScanService {
  @Autowired
  private MediaService mediaService;
  @Autowired
  private MimeTypeService mimeTypeService;

  public void doScan() {
    List<Path> paths = mediaService.listFilesRoot();

    paths.stream().map(path -> new File(path.toUri())).filter(File::canRead)
        .filter(file -> mimeTypeService.isVideoType(file.getAbsolutePath()))
        .forEach(this::createMediaIfNotExisting);
  }

  public void createMediaIfNotExisting(File file) {
    Optional<Media> mediaOptional = mediaService.findByPath(file.getAbsolutePath());
    if (mediaOptional.isPresent())
      return;
    // Save to DB
    Media media = Media.builder().path(file.getAbsolutePath()).build();
    mediaService.save(media);
  }

}
