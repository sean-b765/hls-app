package dev.amethyst.app.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractBaseEntity implements UserDetails {
  @Builder.Default
  @NonNull
  private List<String> roles = new ArrayList<>();
  @NonNull
  @Column(nullable = false, unique = true)
  private String username;
  @NonNull
  @Column(nullable = false)
  private String password;

  @NonNull
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<? extends GrantedAuthority> authorities = this.roles.stream().map(role -> new SimpleGrantedAuthority(role))
        .toList();
    return Objects.requireNonNull(authorities);
  }
}
