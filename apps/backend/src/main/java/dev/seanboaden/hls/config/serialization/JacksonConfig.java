package dev.seanboaden.hls.config.serialization;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
    return builder -> {
      builder.modules(new JavaTimeModule());
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    };
  }

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter();
  }

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder.build();
  }
}
