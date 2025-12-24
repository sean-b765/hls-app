package dev.seanboaden.hls.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.user.model.Role;
import dev.seanboaden.hls.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "CRUD Users")
public class UserController {
  @Autowired
  private UserService userService;

  @PreAuthorize(Role.PreAuthorized.ADMIN)
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable String id) {
    if (id == null)
      return ResponseEntity.notFound().build();
    this.userService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
