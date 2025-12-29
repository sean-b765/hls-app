package dev.amethyst.app.room.exception;

public class RoomNotFoundException extends Throwable {
  public RoomNotFoundException(String message) {
    super(message);
  }

  public RoomNotFoundException() {
    super();
  }
}
