package dev.seanboaden.hls.user.model;

public class Role {
  public final static String ADMIN = "ADMIN";
  public final static String USER = "USER";

  public static final String[] ALL_ROLES = {
      USER,
      ADMIN
  };

  public static class PreAuthorized {
    public static final String ADMIN = "hasAuthority('" + Role.ADMIN + "')";
  }
}
