package dev.amethyst.app.config.event;

import lombok.Data;

@Data
public class UpdateEvent<T> {
  private T payload;

  public UpdateEvent(T payload) {
    this.payload = payload;
  }
}
