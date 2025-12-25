package dev.seanboaden.hls.library.controller;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.library.service.LibraryScanService;
import dev.seanboaden.hls.library.service.LibraryService;
import dev.seanboaden.hls.user.model.Role;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
  @Autowired
  private LibraryService libraryService;
  @Autowired
  private LibraryScanService libraryScanService;
  @Autowired
  private FileSystemService fileSystemService;

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @PostMapping()
  public ResponseEntity<Library> create(@RequestBody Library library) {
    if (library == null)
      return ResponseEntity.badRequest().build();

    if (!this.fileSystemService.isValidDirectory(Path.of(library.getPath())))
      return ResponseEntity.badRequest().build();

    Library saved = this.libraryService.save(library);
    URI location = URI.create("/api/library/" + saved.getId());

    return ResponseEntity.created(Objects.requireNonNull(location)).body(saved);
  }

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @PostMapping("/{id}/scan")
  public ResponseEntity<?> scan(@PathVariable String id) {
    if (id == null)
      return ResponseEntity.badRequest().build();

    Library saved = this.libraryService.findById(id).orElse(null);
    if (saved == null)
      return ResponseEntity.notFound().build();

    // Perform scan
    this.libraryScanService.createNewMediaInLibrary(saved);

    return ResponseEntity.ok().build();
  }

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @PutMapping()
  public ResponseEntity<Library> update(@RequestBody Library library) {
    if (library == null || library.getId() == null)
      return ResponseEntity.badRequest().build();

    if (!this.fileSystemService.isValidDirectory(Path.of(library.getPath())))
      return ResponseEntity.badRequest().build();

    if (this.libraryService.findById(library.getId()).isEmpty())
      return ResponseEntity.notFound().build();

    Library saved = this.libraryService.save(library);

    return ResponseEntity.ok(saved);
  }

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {
    if (id == null)
      return ResponseEntity.badRequest().build();

    this.libraryService.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<Library>> findAll() {
    return ResponseEntity.ok(this.libraryService.findAll());
  }
}
