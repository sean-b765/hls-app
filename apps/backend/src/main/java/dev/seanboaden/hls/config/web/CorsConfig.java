package dev.seanboaden.hls.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        String[] allowedOrigins = new String[] {
            "http://localhost:5173"
        };
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowCredentials(true)
            .allowedMethods("*")
            .exposedHeaders("Authorization");
      }
    };
  }
}
