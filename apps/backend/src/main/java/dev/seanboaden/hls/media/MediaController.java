package dev.seanboaden.hls.media;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media", description = "Manage and retrieve Media")
public class MediaController {
  @Autowired
  private MediaService mediaService;
  @Autowired
  private MediaScanProgressRegistry mediaScanProgressRegistry;

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

  @GetMapping(path = "/scan-progress")
  public Flux<ServerSentEvent<Map<String, MediaProgressEnum>>> streamProgress() {
    return Flux.interval(Duration.ofMillis(250))
        .map(tick -> {
          return ServerSentEvent.<Map<String, MediaProgressEnum>>builder()
              .event("progress")
              .data(mediaScanProgressRegistry.getProgress())
              .build();
        });
  }
}
