package dev.amethyst.app.config.interceptor;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import dev.amethyst.app.config.service.JwtService;
import dev.amethyst.app.user.service.UserService;
import io.jsonwebtoken.JwtException;

/**
 * Attaches the User to the websocket attributes, available using:
 * SessionWrapper::getUser
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;

  @Override
  public boolean beforeHandshake(
      @NonNull ServerHttpRequest request,
      @NonNull ServerHttpResponse response,
      @NonNull WebSocketHandler wsHandler,
      Map<String, Object> attributes) throws Exception {
    Object accessToken = request.getAttributes().get("AccessToken");
    if (accessToken != null) {
      String token = accessToken.toString();
      try {
        String userId = this.jwtService.extractSubject(token);
        if (userId == null) {
          return false;
        }

        this.userService
            .findById(userId)
            .ifPresent(user -> {
              attributes.put("user", user);
            });
      } catch (JwtException ex) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void afterHandshake(
      @NonNull ServerHttpRequest request,
      @NonNull ServerHttpResponse response,
      @NonNull WebSocketHandler wsHandler,
      @Nullable Exception exception) {
    return;
  }

}
