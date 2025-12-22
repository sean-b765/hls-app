package dev.seanboaden.hls.collection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.collection.service.TvSeriesCollectionService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/series")
@Tag(name = "TV Series", description = "Retrieve TV Series")
public class TvSeriesCollectionController {
  @Autowired
  private TvSeriesCollectionService tvSeriesCollectionService;

  @GetMapping
  public ResponseEntity<List<TvSeriesCollection>> findTvSeries() {
    return ResponseEntity.ok(this.tvSeriesCollectionService.findAll());
  }
}
