package dev.seanboaden.hls.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dev.seanboaden.hls.config.filters.JwtAuthenticationFilter;
import dev.seanboaden.hls.user.model.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/user/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/ws").hasAnyRole(Role.allRoles())
            .requestMatchers("/api/admin/**").hasAnyRole(Role.ADMIN)
            .requestMatchers("/api/**").hasAnyRole(Role.allRoles())
            .anyRequest().authenticated());

    httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

}
