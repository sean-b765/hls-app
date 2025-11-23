package dev.seanboaden.hls.config;

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
                registry.addMapping("/static/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET");
                registry.addMapping("/api/playlist/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET");
                registry.addMapping("/api/video/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET");
            }
        };
    }
}

