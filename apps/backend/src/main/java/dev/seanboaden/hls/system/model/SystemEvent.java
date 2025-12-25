package dev.seanboaden.hls.system.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "system_event")
@Data
@Builder
public class SystemEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private SystemEventType type;
  private String message;
  private String resourceId;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
