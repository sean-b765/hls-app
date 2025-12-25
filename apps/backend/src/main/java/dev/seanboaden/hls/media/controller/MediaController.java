package dev.seanboaden.hls.media.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media", description = "Manage and retrieve Media")
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

  @GetMapping(path = "/{id}")
  public ResponseEntity<Media> getById(@PathVariable String id) {
    Optional<Media> optionalMedia = mediaService.findById(id);
    if (optionalMedia.isEmpty())
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok(optionalMedia.get());
  }
}
