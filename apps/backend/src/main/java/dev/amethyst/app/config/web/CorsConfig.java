package dev.amethyst.app.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    List<String> allowedOrigins = List.of("http://localhost:5173");
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(allowedOrigins);
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization", "X-Hls-Token"));
    config.setAllowCredentials(true);

    CorsConfiguration hlsConfig = new CorsConfiguration();
    hlsConfig.setAllowedOrigins(allowedOrigins);
    hlsConfig.setAllowedMethods(List.of("*"));
    hlsConfig.setAllowedHeaders(List.of("*"));
    hlsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/video/**", hlsConfig);
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
