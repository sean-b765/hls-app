package dev.amethyst.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {
    "dev.seanboaden.hls"
})
@EnableScheduling
public class HlsApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(HlsApplication.class, args);
  }

}
