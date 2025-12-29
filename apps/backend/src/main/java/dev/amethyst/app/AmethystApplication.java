package dev.amethyst.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {
    "dev.amethyst.app"
})
@EnableScheduling
public class AmethystApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(AmethystApplication.class, args);
  }

}
