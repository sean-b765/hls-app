package dev.seanboaden.hls.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MimeConfig {

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter();
  }

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
