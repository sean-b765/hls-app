package dev.seanboaden.hls.collection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/series")
@Tag(name = "TV Series", description = "Manage and retrieve TV Series")
public class TvSeriesCollectionController {
  @Autowired
  private TvSeriesCollectionService mediaCollectionService;

  @GetMapping
  public ResponseEntity<List<TvSeriesCollection>> findAll() {
    return ResponseEntity.ok(this.mediaCollectionService.findAll());
  }
}
