package dev.amethyst.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.amethyst.app.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {
  public Optional<User> findByUsername(String username);
}
