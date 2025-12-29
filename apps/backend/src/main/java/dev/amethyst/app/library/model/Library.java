package dev.amethyst.app.library.model;

import org.springframework.lang.NonNull;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import dev.amethyst.app.library.handler.LibraryEventListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "library")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@EntityListeners(value = { LibraryEventListener.class })
public class Library extends AbstractBaseEntity {
  @Column(nullable = false)
  @NonNull
  private String name;
  @Column(unique = true, nullable = false)
  @NonNull
  private String path;
  @Column(nullable = false)
  @NonNull
  private LibraryType type;
  private Integer orderIndex;
}
