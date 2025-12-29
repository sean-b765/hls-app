package dev.seanboaden.hls.library.controller;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.config.base.AbstractCrudController;
import dev.seanboaden.hls.filesystem.service.FileSystemService;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.library.repository.LibraryRepository;
import dev.seanboaden.hls.library.service.LibraryScanService;
import dev.seanboaden.hls.library.service.LibraryService;
import dev.seanboaden.hls.user.model.Role;

@RestController
@RequestMapping("/api/library")
public class LibraryController extends AbstractCrudController<Library, String, LibraryRepository, LibraryService> {
  @Autowired
  private LibraryScanService libraryScanService;
  @Autowired
  private FileSystemService fileSystemService;

  protected LibraryController(LibraryService service) {
    super(service);
  }

  @Override
  protected void canCreate(Library body) {
    if (!this.fileSystemService.isValidDirectory(Path.of(body.getPath())))
      throw new IllegalAccessError();
    if (!this.hasAuthority(Role.ADMIN))
      throw new AccessDeniedException(Role.Messages.ADMIN);
  }

  @Override
  protected void canUpdate(Library body) {
    if (!this.fileSystemService.isValidDirectory(Path.of(body.getPath())))
      throw new IllegalAccessError(body.getPath());
    if (!this.hasAuthority(Role.ADMIN))
      throw new AccessDeniedException(Role.Messages.ADMIN);
  }

  @Override
  protected void canDelete(String id) {
    if (!this.hasAuthority(Role.ADMIN))
      throw new AccessDeniedException(Role.Messages.ADMIN);
  }

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @PostMapping("/{id}/scan")
  public ResponseEntity<?> scan(@PathVariable String id) {
    if (id == null)
      return ResponseEntity.badRequest().build();

    Library saved = this.service.findById(id).orElse(null);
    if (saved == null)
      return ResponseEntity.notFound().build();

    // Perform scan
    this.libraryScanService.createNewMediaInLibrary(saved);

    return ResponseEntity.ok().build();
  }
}
