package dev.seanboaden.hls.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private MediaRepository mediaRepository;

    @PutMapping
    public ResponseEntity<Media> save(@RequestBody Media media) {
        Media theMedia = mediaRepository.save(media);
        return ResponseEntity.ok(theMedia);
    }
}
