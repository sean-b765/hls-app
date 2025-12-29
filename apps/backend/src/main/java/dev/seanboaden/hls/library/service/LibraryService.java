package dev.seanboaden.hls.library.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.library.repository.LibraryRepository;

@Service
public class LibraryService extends AbstractCrudService<Library, String, LibraryRepository> {
  protected LibraryService(LibraryRepository repository) {
    super(repository);
  }

  public Optional<Library> findByPath(String path) {
    return this.repository.findByPath(path);
  }
}
