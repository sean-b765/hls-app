package dev.seanboaden.hls.config.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    this.extractAccessTokenFromAuthorizationHeader(request);
    this.extractAccessTokenFromWsHeader(request);
    filterChain.doFilter(request, response);
  }

  private void extractAccessTokenFromWsHeader(@NonNull HttpServletRequest request) {
    final String header = request.getHeader("Sec-WebSocket-Protocol");
    if (header == null || header.isEmpty())
      return;

    String accessToken = header.replace("Authorization, Bearer%20", "");

    // Extract the access token from header, place in our request scope
    request.setAttribute("AccessToken", accessToken);
  }

  private void extractAccessTokenFromAuthorizationHeader(@NonNull HttpServletRequest request) {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || header.isEmpty() || !header.startsWith("Bearer "))
      return;

    final String accessToken = header.split(" ")[1].trim();
    if (accessToken.isEmpty())
      return;

    // place in request scope
    request.setAttribute("AccessToken", accessToken);
  }
}
