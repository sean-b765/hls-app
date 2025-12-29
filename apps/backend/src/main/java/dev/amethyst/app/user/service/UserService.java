package dev.amethyst.app.user.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import dev.amethyst.app.config.base.AbstractCrudService;
import dev.amethyst.app.user.model.User;
import dev.amethyst.app.user.repository.UserRepository;

@Service
public class UserService extends AbstractCrudService<User, String, UserRepository> {

  protected UserService(UserRepository repository) {
    super(repository);
  }

  public Optional<User> findByUsername(String username) {
    return this.repository.findByUsername(username);
  }
}
