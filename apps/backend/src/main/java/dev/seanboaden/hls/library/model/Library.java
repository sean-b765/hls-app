package dev.seanboaden.hls.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "library")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Library {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String path;
  @Column(nullable = false)
  private LibraryType type;
  private Integer orderIndex;
}
