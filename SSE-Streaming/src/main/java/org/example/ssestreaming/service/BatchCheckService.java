package org.example.ssestreaming.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchCheckService {
    private static final int BATCH_SIZE = 100;
    private static final int MAX_CONCURRENCY = 10;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final AtomicInteger running = new AtomicInteger(0);
    private final AtomicInteger maxObserved = new AtomicInteger(0);

    public void process(List<Account> accounts) {
        List<List<Account>> batches = splitBatch(accounts, BATCH_SIZE);
        Semaphore sem = new Semaphore(MAX_CONCURRENCY);
        AtomicInteger batchSeq = new AtomicInteger(0);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (List<Account> batch : batches) {
            int batchNo = batchSeq.incrementAndGet();

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    sem.acquire();
                    int current = running.incrementAndGet();
                    maxObserved.updateAndGet(max -> Math.max(max, current));

                    log("BATCH-" + batchNo, "START (running=" + current + ")");

                    // Xử lý uppercase name - modify trực tiếp
                    batch.forEach(account -> account.setName(account.getName().toUpperCase()));

                    log("BATCH-" + batchNo, "DONE - Processed " + batch.size() + " accounts");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log("BATCH-" + batchNo, "INTERRUPTED");
                } finally {
                    int current = running.decrementAndGet();
                    sem.release();
                    log("BATCH-" + batchNo, "RELEASED (running=" + current + ")");
                }
            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log("SUMMARY", "Max concurrent batches: " + maxObserved.get());

    }


    private static void log(String tag, String msg) {
        log.info("{} [{}] {} {}", LocalTime.now(), Thread.currentThread(), tag, msg);
    }

    private static <T> List<List<T>> splitBatch(List<T> data, int batchSize) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += batchSize) {
            result.add(data.subList(i, Math.min(i + batchSize, data.size())));
        }
        return result;
    }

    @Getter
    @Setter
    public static class Account {
        private String id;
        private String name;

        public Account(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
