package dev.seanboaden.hls.system.model;

import dev.seanboaden.hls.config.base.AbstractBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "system_event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SystemEvent extends AbstractBaseEntity {
  private SystemEventType type;
  private String message;
  private String resourceId;
}
