package dev.seanboaden.hls.media;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  @GetMapping(path = "/{id}")
  public ResponseEntity<Media> getById(@PathVariable String id) {
    Optional<Media> optionalMedia = mediaService.findById(id);
    if (optionalMedia.isEmpty())
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok(optionalMedia.get());
  }

  @GetMapping(path = "/scan-progress")
  public Flux<ServerSentEvent<String>> streamProgress() {
    return Flux.interval(Duration.ofMillis(1000))
        .map(tick -> {
          String json = "{}";
          try {
            json = new ObjectMapper().writeValueAsString(mediaScanProgressRegistry.getProgress());
          } catch (JsonProcessingException ignored) {
          }
          return ServerSentEvent.<String>builder()
              .event("progress")
              .data(json)
              .build();
        });
  }
}
