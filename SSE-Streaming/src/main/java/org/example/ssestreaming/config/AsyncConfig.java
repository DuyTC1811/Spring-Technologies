package org.example.ssestreaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Bean("applicationTaskExecutor")
    SimpleAsyncTaskExecutor executor() {
        SimpleAsyncTaskExecutor exec = new SimpleAsyncTaskExecutor("vt-");
        exec.setVirtualThreads(true);
        return exec;
    }

}
