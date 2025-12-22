package dev.seanboaden.hls.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MimeConfig {
  @Bean
  public WebMvcConfigurer mimeConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void configureContentNegotiation(@NonNull ContentNegotiationConfigurer configurer) {
        configurer.mediaType("ts", MediaType.valueOf("video/mp2t"));
        configurer.mediaType("m3u8", MediaType.valueOf("application/vnd.apple.mpegurl"));
      }
    };
  }
}
