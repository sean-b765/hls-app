package dev.seanboaden.hls.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.seanboaden.hls.config.service.JwtService;
import dev.seanboaden.hls.user.dto.AuthRequest;
import dev.seanboaden.hls.user.model.User;
import dev.seanboaden.hls.user.service.AuthService;
import dev.seanboaden.hls.user.service.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication and signup")
public class AuthController {
  @Value("${security.jwt.refresh-token.expiration-time}")
  private long refreshTokenExpirationMs;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;
  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
    User user = null;
    Optional<User> existingUserByUsername = this.userService.findByUsername(request.getUsername());
    if (existingUserByUsername.isEmpty()) {
      throw new UsernameNotFoundException("User " + request.getUsername() + " was not found.");
    }

    String userId = existingUserByUsername.get().getId();
    try {
      user = this.authService.authenticate(userId, request.getPassword());
      String accessToken = jwtService.generateAccessToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);

      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      cookie.setMaxAge((int) (refreshTokenExpirationMs / 1000));
      response.addCookie(cookie);

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, accessToken)
          .build();
    } catch (BadCredentialsException ex) {
      throw ex;
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, "")
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

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
    if (refreshToken == null || refreshToken.isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .build();
    }

    try {
      String userId = this.jwtService.extractSubject(refreshToken);
      Optional<User> existingUser = this.userService.findById(userId);
      if (existingUser.isEmpty()) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build();
      }

      // generate the new accessToken
      String newAccessToken = this.jwtService.generateAccessToken(existingUser.get());

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, newAccessToken)
          .build();
    } catch (JwtException e) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .build();
    }
  }

}
