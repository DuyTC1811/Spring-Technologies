package org.example.ssestreaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "virtualThread")
    public ExecutorService executor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

}
