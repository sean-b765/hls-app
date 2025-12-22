package dev.seanboaden.hls.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.seanboaden.hls.config.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private AntPathMatcher pathMatcher = new AntPathMatcher();

  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final Object tokenAttribute = request.getAttribute("AccessToken");

    if (tokenAttribute == null) {
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = tokenAttribute.toString();

    try {
      if (this.jwtService.isTokenExpired(accessToken)) {
        return;
      }

      final String username = this.jwtService.extractUsername(accessToken);

      UserDetails user = this.userDetailsService.loadUserByUsername(username);

      if (user == null) {
        return;
      }

      if (!this.jwtService.isTokenValid(accessToken, user)) {
        return;
      }

      Collection<? extends GrantedAuthority> authorities = user == null ? Collections.emptyList()
          : user.getAuthorities();

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          user,
          null,
          authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
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
        add("/api/users/login");
        add("/api/users/signup");
      }
    };
    return pathsToSkip.stream().anyMatch(p -> this.pathMatcher.match(p, request.getServletPath()));
  }
}
