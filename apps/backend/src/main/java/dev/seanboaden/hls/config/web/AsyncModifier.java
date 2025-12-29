package dev.seanboaden.hls.config.web;

public class AsyncModifier {
  public static class Prefixes {
    public static final String MVC = "mvc-executor-";
    public static final String SQLITE = "sqlite-executor-";
  }

  public static class Modifier {
    /**
     * SQLITE: single-threaded executor for use with asynchronous writing to DB
     */
    public static final String SQLITE = "sqlite-executor";
    /**
     * HLS: executor for use with HLS stream endpoints
     */
    public static final String HLS = "hls-executor";
  }
}
