package dev.seanboaden.hls.config.base;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AbstractBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @NonNull
  private String id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @NonNull
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Column(nullable = true, updatable = true)
  private LocalDateTime updatedAt;
}
