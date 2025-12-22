package dev.seanboaden.hls.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.seanboaden.hls.user.dto.AuthRequest;
import dev.seanboaden.hls.user.model.Role;
import dev.seanboaden.hls.user.model.User;

@Service
public class AuthService {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public User signup(AuthRequest request) {
    User userSignupRequest = User.builder()
        .username(request.getUsername())
        .password(this.passwordEncoder.encode(request.getPassword()))
        .roles(List.of(Role.USER))
        .build();
    return this.userService.save(userSignupRequest);
  }

  public User authenticate(AuthRequest request) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    return (User) authentication.getPrincipal();
  }
}
