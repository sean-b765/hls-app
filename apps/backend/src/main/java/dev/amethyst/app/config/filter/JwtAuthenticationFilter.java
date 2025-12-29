package dev.amethyst.app.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.amethyst.app.config.scope.UserRequestScope;
import dev.amethyst.app.config.service.JwtService;
import dev.amethyst.app.user.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private AntPathMatcher pathMatcher = new AntPathMatcher();

  @Autowired
  private UserRequestScope userRequestScope;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    this.extractAccessTokenFromAuthorizationHeader(request);
    this.extractAccessTokenFromWsHeader(request);

    final Object tokenAttribute = request.getAttribute("AccessToken");

    if (tokenAttribute == null) {
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = tokenAttribute.toString();

    try {
      if (this.jwtService.isTokenExpired(accessToken)) {
        System.out.println("expired token");
        throw new ExpiredJwtException(null, null, accessToken);
      }

      final String userId = this.jwtService.extractSubject(accessToken);

      UserDetails user = this.userDetailsService.loadUserByUsername(userId);

      if (user == null) {
        throw new AccessDeniedException("User was not found.");
      }

      if (!this.jwtService.isTokenValid(accessToken, user)) {
        System.out.println("invalid token");
        return;
      }

      Collection<? extends GrantedAuthority> authorities = user == null ? Collections.emptyList()
          : user.getAuthorities();

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          user,
          null,
          authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      // Set security context with authentication
      SecurityContext context = SecurityContextHolder.getContext();
      context.setAuthentication(authentication);
      this.userRequestScope.setUser((User) user);
    } catch (Exception ex) {
      System.out.println("JWT Authentication Error: " + ex.getMessage());
      response.setStatus(403);
      return;
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    List<String> pathsToSkip = new ArrayList<>() {
      {
        add("/auth/**");
        // Disable on HLS endpoint (secured with X-Hls-Token)
        add("/api/video/**");
      }
    };
    return pathsToSkip.stream().anyMatch(p -> this.pathMatcher.match(p, request.getServletPath()));
  }

  private void extractAccessTokenFromWsHeader(@NonNull HttpServletRequest request) {
    final String header = request.getHeader("Sec-WebSocket-Protocol");
    if (header == null || header.isEmpty())
      return;

    String accessToken = header.replace("Bearer%20", "");

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
