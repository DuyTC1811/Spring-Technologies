package com.example.springasync.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration {
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);                            // KHI CÓ REQUEST SẼ TẠO MẶC ĐỊNH (setCorePoolSize)
        taskExecutor.setQueueCapacity(1000);                        // SỐ LƯỢNG TỐI ĐA (BlockingQueue) HÀNG ĐỢI
        taskExecutor.setMaxPoolSize(5);                             // SỐ LƯỢNG TỐI ĐA THREAD TRONG POOL (setMaxPoolSize)
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);     // SẼ ĐỢI CÁC TASK ĐƯỢC HOÀN THÀNH SAU ĐÓ DỪNG HOẶC HUỶ BỎ
        taskExecutor.setThreadNamePrefix("TaskThread -> ");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
