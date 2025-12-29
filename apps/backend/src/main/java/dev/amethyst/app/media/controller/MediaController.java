package dev.amethyst.app.media.controller;

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

import dev.amethyst.app.config.base.AbstractCrudController;
import dev.amethyst.app.media.model.Media;
import dev.amethyst.app.media.repository.MediaRepository;
import dev.amethyst.app.media.service.MediaService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media", description = "Manage and retrieve Media")
public class MediaController extends AbstractCrudController<Media, String, MediaRepository, MediaService> {
  protected MediaController(MediaService service) {
    super(service);
  }
}
