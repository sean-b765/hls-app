package dev.seanboaden.hls.session.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoResponse extends ResponseConverter {
  @Builder.Default
  private String type = "INFO";
  private String message;
}
