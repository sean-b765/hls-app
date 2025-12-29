package dev.seanboaden.hls.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler(IllegalAccessError.class)
  public ResponseEntity<ErrorResponse> handle(IllegalAccessException ex, HttpServletRequest req) {
    return build(500, ex, req);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handle(BadCredentialsException ex, HttpServletRequest req) {
    return build(400, ex, req);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handle(UsernameNotFoundException ex, HttpServletRequest req) {
    return build(400, ex, req);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handle(AccessDeniedException ex, HttpServletRequest req) {
    return build(403, ex, req);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handle(Exception ex, HttpServletRequest req) {
    return build(500, ex, req);
  }

  private ResponseEntity<ErrorResponse> build(int statusCode, Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(statusCode)
        .body(ErrorResponse.builder()
            .error(ex.getClass().getSimpleName())
            .status(statusCode)
            .message(ex.getMessage())
            .build());
  }
}
