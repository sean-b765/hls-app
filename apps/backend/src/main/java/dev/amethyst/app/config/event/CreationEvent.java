package dev.amethyst.app.config.event;

import lombok.Data;

@Data
public class CreationEvent<T> {
  private T payload;

  public CreationEvent(T payload) {
    this.payload = payload;
  }
}
