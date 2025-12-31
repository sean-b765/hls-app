package dev.amethyst.app.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.library.model.Library;
import dev.amethyst.app.library.repository.LibraryRepository;

@Service
public class LibraryService extends AbstractCrudService<Library, String, LibraryRepository> {
  public Optional<Library> findByPath(String path) {
    return this.repository.findByPath(path);
  }
}
