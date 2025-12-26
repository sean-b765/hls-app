package dev.seanboaden.hls.collection.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.collection.model.TvSeriesCollection;
import dev.seanboaden.hls.collection.repository.TvSeriesCollectionRepository;
import dev.seanboaden.hls.collection.service.TvSeriesCollectionService;
import dev.seanboaden.hls.config.base.AbstractCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/series")
@Tag(name = "TV Series", description = "Retrieve TV Series")
public class TvSeriesCollectionController extends
    AbstractCrudController<TvSeriesCollection, String, TvSeriesCollectionRepository, TvSeriesCollectionService> {
  protected TvSeriesCollectionController(TvSeriesCollectionService service) {
    super(service);
  }
}
