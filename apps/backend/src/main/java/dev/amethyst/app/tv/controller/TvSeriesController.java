package dev.amethyst.app.tv.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.amethyst.app.config.base.AbstractCrudController;
import dev.amethyst.app.tv.model.TvSeries;
import dev.amethyst.app.tv.repository.TvSeriesRepository;
import dev.amethyst.app.tv.service.TvSeriesService;
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
