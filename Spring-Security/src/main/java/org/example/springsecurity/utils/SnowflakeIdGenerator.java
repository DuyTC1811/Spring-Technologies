package org.example.springsecurity.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class SnowflakeIdGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final long EPOCH = 1735689600000L; // 2025-01-01 00:00:00 UTC

    private static final long NODE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_NODE_ID = ~(-1L << NODE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long NODE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;

    private final long nodeId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(String fileName) {
        return fileName + "_" + UUID.randomUUID().toString().replace("-", "");
    }

    private static String random(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("nodeId must be between 0 and " + MAX_NODE_ID);
        }
        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - currentTimestamp) + " ms");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT) | (nodeId << NODE_ID_SHIFT) | sequence;
    }

    private long waitUntilNextMillis(long currentTimestamp) {
        long now = System.currentTimeMillis();
        while (now <= currentTimestamp) {
            now = System.currentTimeMillis();
        }
        return now;
    }


//    public static String generate(String fileName) {
//        return fileName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
//    }

//    public static String generate(String fileName) {
//        SnowflakeIdGenerator randomIdGenerator = new SnowflakeIdGenerator(1);
//        return fileName + "_" + randomIdGenerator.nextId();
//    }

    public static void main(String[] args) throws InterruptedException {
//        int nextInt = ThreadLocalRandom.current().nextInt(1, 11);
//        System.out.println(nextInt);
        for (int i = 0; i < 10; i++) {
            int nextInt = SECURE_RANDOM.nextInt(10) + 1;
            System.out.println(nextInt);
        }
//        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1L);
//        int THREADS = 100;
//        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
//
//        Set<String> results = ConcurrentHashMap.newKeySet();
//
//        CountDownLatch latch = new CountDownLatch(THREADS);
//
//        for (int i = 0; i < THREADS; i++) {
////            Long id = generator.nextId();
////            String noname = generate("noname");
////            if (!results.add(noname)) {
////                System.out.println("💥 DUPLICATE: " + noname);
////            }
//            executor.submit(() -> {
//                try {
////                    Long id = generator.nextId();;
//                    String noname = generate("noname");
//
//                    if (!results.add(noname)) {
//                        System.out.println("💥 DUPLICATE: " + noname);
//                    }
//
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        executor.shutdown();
//
//        System.out.println("Total unique IDs: " + results.size());
    }
}
