package dev.seanboaden.hls.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.model.Media;
import dev.seanboaden.hls.media.service.MediaService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Retrieve Movies")
public class MovieCollectionController {
  @Autowired
  private MediaService mediaService;

  @GetMapping
  public ResponseEntity<List<Media>> findMovies() {
    return ResponseEntity.ok(this.mediaService.findAllWhereTvSeasonIsNull());
  }
}
