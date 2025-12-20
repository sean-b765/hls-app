package dev.seanboaden.hls.media.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.model.MediaMetadata;
import dev.seanboaden.hls.media.service.MediaMetadataService;

@RestController
@RequestMapping("/api/metadata")
public class MediaMetadataController {
  @Autowired
  private MediaMetadataService mediaMetadataService;

  @GetMapping
  public ResponseEntity<List<MediaMetadata>> getAll() {
    List<MediaMetadata> theMedias = mediaMetadataService.findAll();
    return ResponseEntity.ok(theMedias);
  }
}
