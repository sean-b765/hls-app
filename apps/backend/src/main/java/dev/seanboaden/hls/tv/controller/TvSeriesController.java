package dev.seanboaden.hls.tv.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.config.base.AbstractCrudController;
import dev.seanboaden.hls.tv.model.TvSeries;
import dev.seanboaden.hls.tv.repository.TvSeriesRepository;
import dev.seanboaden.hls.tv.service.TvSeriesService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/series")
@Tag(name = "TV Series", description = "Retrieve TV Series")
public class TvSeriesController extends
    AbstractCrudController<TvSeries, String, TvSeriesRepository, TvSeriesService> {
  protected TvSeriesController(TvSeriesService service) {
    super(service);
  }
}
