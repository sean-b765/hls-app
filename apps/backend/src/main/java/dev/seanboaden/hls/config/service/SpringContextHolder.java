package dev.seanboaden.hls.config.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Because the spring context isn't always available,
 * wanna use this to expose the context in a static holder
 * This way we can get the beanz
 */
@Component
public class SpringContextHolder {
  private static ApplicationContext context;

  public SpringContextHolder(ApplicationContext context) {
    SpringContextHolder.context = context;
  }

  public static <T> T getBean(Class<T> clazz) {
    return context.getBean(clazz);
  }
}
