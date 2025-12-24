package dev.seanboaden.hls.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.library.model.Library;
import dev.seanboaden.hls.library.repository.LibraryRepository;

@Service
public class LibraryService {
  @Autowired
  private LibraryRepository libraryRepository;

  public Library save(@NonNull Library library) {
    return this.libraryRepository.save(library);
  }

  public Optional<Library> findByPath(String path) {
    return this.libraryRepository.findByPath(path);
  }

  public List<Library> findAll() {
    return this.libraryRepository.findAll();
  }

  public void delete(@NonNull String id) {
    this.libraryRepository.deleteById(id);
  }
}
