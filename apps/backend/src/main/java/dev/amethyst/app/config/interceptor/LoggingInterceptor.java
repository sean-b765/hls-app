package dev.amethyst.app.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
  private final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler) throws Exception {
    log.trace(String.format("%-10s%s", "[" + request.getMethod() + "]", request.getRequestURI()));

    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null && securityContext.getAuthentication() != null) {
      log.trace(String.format("%-10sUser: %s", "", securityContext.getAuthentication().getName()));
    }

    return true;
  }

}
