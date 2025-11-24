package org.example.ssestreaming.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class DemoAsyncService {

    @Async   // sẽ dùng applicationTaskExecutor
    public void doWorkAsync(String input) {

        long start = System.currentTimeMillis();
        Thread current = Thread.currentThread();

        log.info("[ASYNC-START] threadName={}, isVirtual={}, input={} at {}",
                current.getName(),
                current.isVirtual(),
                input,
                start);

        // mô phỏng xử lý
        try {
            Thread.sleep(100); // chỉ demo, thực tế có thể là đọc file, call API, v.v.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long end = System.currentTimeMillis();
        long duration = end - start;

        log.info("[ASYNC-END] threadName={}, isVirtual={}, input={}, duration={} ms",
                current.getName(),
                current.isVirtual(),
                input,
                duration);
        // trả về true nếu đang chạy trên virtual thread
        CompletableFuture.completedFuture(current.isVirtual());
    }
}
