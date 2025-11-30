package dev.seanboaden.hls.collection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.media.Media;
import dev.seanboaden.hls.media.MediaService;
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
