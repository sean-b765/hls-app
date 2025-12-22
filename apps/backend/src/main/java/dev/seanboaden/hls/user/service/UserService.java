package dev.seanboaden.hls.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.user.model.User;
import dev.seanboaden.hls.user.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public Optional<User> findByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  public User save(@NonNull User user) {
    return this.userRepository.save(user);
  }

  public void deleteById(@NonNull String id) {
    this.userRepository.deleteById(id);
  }
}
