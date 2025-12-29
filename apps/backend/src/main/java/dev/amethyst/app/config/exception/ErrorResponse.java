package dev.amethyst.app.config.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();
  private Integer status;
  private String error;
  private String message;
}
