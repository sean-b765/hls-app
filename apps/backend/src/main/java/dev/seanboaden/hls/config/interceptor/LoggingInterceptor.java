package dev.seanboaden.hls.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
  private final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @PostConstruct
  public void init() {
    log.info("Enabled logging");
  }

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler) throws Exception {
    log.info(String.format("[%s] %s", request.getMethod(), request.getRequestURI()));

    SecurityContext securityContext = SecurityContextHolder.getContext();
    Object accessTokenAttribute = request.getAttribute("AccessToken");

    if (accessTokenAttribute != null)
      log.info(String.format("\t-%s...", accessTokenAttribute.toString().substring(0, 10)));

    log.info(String.format("\t-%s", securityContext));

    return true;
  }

}
