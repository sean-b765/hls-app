package dev.seanboaden.hls.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.config.base.AbstractCrudController;
import dev.seanboaden.hls.user.model.Role;
import dev.seanboaden.hls.user.model.User;
import dev.seanboaden.hls.user.repository.UserRepository;
import dev.seanboaden.hls.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "CRUD Users")
public class UserController extends AbstractCrudController<User, String, UserRepository, UserService> {
  protected UserController(UserService service) {
    super(service);
  }

  @Override
  protected void canDelete(String id) {
    if (!this.hasAuthority(Role.ADMIN))
      throw new AccessDeniedException(Role.Messages.ADMIN);
  }
}
