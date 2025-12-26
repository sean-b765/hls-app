package dev.seanboaden.hls.user.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.config.base.AbstractCrudService;
import dev.seanboaden.hls.user.model.User;
import dev.seanboaden.hls.user.repository.UserRepository;

@Service
public class UserService extends AbstractCrudService<User, String, UserRepository> {

  protected UserService(UserRepository repository) {
    super(repository);
  }

  public Optional<User> findByUsername(String username) {
    return this.repository.findByUsername(username);
  }
}
