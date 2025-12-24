package dev.seanboaden.hls.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.library.model.Library;

public interface LibraryRepository extends JpaRepository<Library, String> {
  Optional<Library> findByPath(String path);
}
