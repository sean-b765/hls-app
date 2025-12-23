package dev.seanboaden.hls.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.seanboaden.hls.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {
  public Optional<User> findByUsername(String username);
}
