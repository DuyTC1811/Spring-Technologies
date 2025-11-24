package org.example.ssestreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SseStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseStreamingApplication.class, args);
    }

}
