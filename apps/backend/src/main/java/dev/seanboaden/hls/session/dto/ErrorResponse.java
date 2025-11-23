package dev.seanboaden.hls.session.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends ResponseConverter {
    @Builder.Default
    private String type = "ERROR";
    private String message;
}