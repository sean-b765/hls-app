package dev.seanboaden.hls.media;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media")
public class MediaController {
  @Autowired
  private MediaService mediaService;

  @PutMapping
  public ResponseEntity<Media> save(@RequestBody Media media) {
    Media theMedia = mediaService.save(media);
    return ResponseEntity.ok(theMedia);
  }

  @GetMapping
  public ResponseEntity<List<Media>> getAll() {
    List<Media> theMedias = mediaService.findAll();
    return ResponseEntity.ok(theMedias);
  }
}
