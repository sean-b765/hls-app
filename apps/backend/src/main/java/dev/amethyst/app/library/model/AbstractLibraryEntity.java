package dev.amethyst.app.library.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.amethyst.app.config.base.AbstractBaseEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractLibraryEntity extends AbstractBaseEntity {
  @ManyToOne
  @JoinColumn(name = "libraryId", referencedColumnName = "id")
  @JsonManagedReference
  private Library library;
}
