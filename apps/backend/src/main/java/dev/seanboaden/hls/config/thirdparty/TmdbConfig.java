package dev.seanboaden.hls.config.thirdparty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import info.movito.themoviedbapi.TmdbApi;

@Component
public class TmdbConfig {
  @Value("${configuration.tmdb.apikey}")
  private String tmdbApiKey;

  @Bean
  public TmdbApi tmdbApi() {
    return new TmdbApi(this.tmdbApiKey);
  }
}
