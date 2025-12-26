package dev.seanboaden.hls.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler(IllegalAccessError.class)
  public ResponseEntity<ErrorResponse> handle(IllegalAccessException ex, HttpServletRequest req) {
    return build(ex, req);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handle(AccessDeniedException ex, HttpServletRequest req) {
    return build(ex, req);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handle(Exception ex, HttpServletRequest req) {
    return build(ex, req);
  }

  private ResponseEntity<ErrorResponse> build(Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(500)
        .body(ErrorResponse.builder()
            .error(ex.getClass().getSimpleName())
            .status(500)
            .message(ex.getMessage())
            .build());
  }
}
