package dev.amethyst.app.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.library.model.Library;

public interface LibraryRepository extends JpaRepository<Library, String> {
  Optional<Library> findByPath(String path);
}
