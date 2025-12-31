package dev.amethyst.app.config.event;

import lombok.Data;

@Data
public class DeletionEvent<T> {
  private T payload;

  public DeletionEvent(T payload) {
    this.payload = payload;
  }
}
