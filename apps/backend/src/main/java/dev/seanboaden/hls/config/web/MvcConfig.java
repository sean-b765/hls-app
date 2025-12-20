package dev.seanboaden.hls.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
  @Value("${transcode.directory}")
  private String transcodeDirectory;

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    String transcodeDirectoryUri = "file:" + transcodeDirectory + (transcodeDirectory.endsWith("/") ? "" : "/");
    registry.addResourceHandler("/static/**")
        .addResourceLocations(transcodeDirectoryUri);
  }
}
