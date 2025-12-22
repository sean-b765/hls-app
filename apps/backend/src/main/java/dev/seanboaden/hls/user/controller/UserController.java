package dev.seanboaden.hls.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.config.service.JwtService;
import dev.seanboaden.hls.user.dto.AuthRequest;
import dev.seanboaden.hls.user.model.User;
import dev.seanboaden.hls.user.service.AuthService;
import dev.seanboaden.hls.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Sign-up and authentication")
public class UserController {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;
  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    User user = this.authService.authenticate(request);

    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, jwtService.generateToken(user))
        .build();
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody AuthRequest request) {
    Optional<User> existingUser = this.userService.findByUsername(request.getUsername());
    if (existingUser.isPresent()) {
      return ResponseEntity.badRequest().body("Username taken");
    }

    User user = this.authService.signup(request);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable String id) {
    if (id == null)
      return ResponseEntity.notFound().build();
    this.userService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
