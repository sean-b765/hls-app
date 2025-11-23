package dev.seanboaden.hls.logging;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
    public static Logger logger = Logger.getLogger("Messages");

    public static void info(String log) {
        logger.info(log);
    }

    public static void error(String log) {
        logger.log(Level.SEVERE, log);
    }
}
