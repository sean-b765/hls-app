package dev.seanboaden.hls.user.model;

public class Role {
  public final static String ADMIN = "ADMIN";
  public final static String USER = "USER";

  public static String[] allRoles() {
    return new String[] {
        USER,
        ADMIN
    };
  }
}
