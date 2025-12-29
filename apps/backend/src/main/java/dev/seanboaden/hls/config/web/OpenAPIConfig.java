package dev.seanboaden.hls.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
public class OpenAPIConfig {
  @Bean
  public OpenAPI api() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("Authorization", securityScheme("Authorization")))
        .addSecurityItem(new SecurityRequirement().addList("Authorization"))
        .info(new Info().title("HLS App").version("1.0.0"));
  }

  private SecurityScheme securityScheme(String name) {
    return new io.swagger.v3.oas.models.security.SecurityScheme()
        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY)
        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
        .name(name);
  }

}
