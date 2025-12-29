package dev.seanboaden.hls.config.web;

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
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization", "X-Hls-Token"));
    config.setAllowCredentials(true);

    CorsConfiguration hlsConfig = new CorsConfiguration();
    hlsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
    hlsConfig.setAllowedMethods(List.of("*"));
    hlsConfig.setAllowedHeaders(List.of("*"));
    hlsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/video/**", hlsConfig);
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
