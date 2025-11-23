package dev.seanboaden.hls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(
        scanBasePackages = {
                "dev.seanboaden.hls"
        }
)
public class HlsApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(HlsApplication.class, args);
    }

}
