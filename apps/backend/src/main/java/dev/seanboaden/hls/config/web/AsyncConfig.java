package dev.seanboaden.hls.config.web;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class AsyncConfig implements WebMvcConfigurer {
  @Override
  public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    configurer.setTaskExecutor(mvcTaskExecutor());
    configurer.setDefaultTimeout(60_000);
  }

  @Bean
  public AsyncTaskExecutor mvcTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix(AsyncExecutor.Prefixes.MVC);
    executor.initialize();
    return executor;
  }

  /**
   * Used for writing to SQLite concurrently
   * Pool size must be set to one as SQLite doesn't handle concurrent write well
   */
  @Bean(name = AsyncExecutor.Names.SQLITE)
  public Executor sqliteExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(1);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix(AsyncExecutor.Prefixes.SQLITE);
    executor.initialize();
    return executor;
  }
}
