package dev.seanboaden.hls.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
  public static Logger logger = Logger.getLogger("Messages");

  static {
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);
    logger.addHandler(handler);
    logger.setUseParentHandlers(false); // disable duplicate logs
    logger.setLevel(Level.ALL);
  }

  public static void info(String log) {
    logger.info(log);
  }

  public static void error(String log) {
    logger.log(Level.SEVERE, log);
  }
}
