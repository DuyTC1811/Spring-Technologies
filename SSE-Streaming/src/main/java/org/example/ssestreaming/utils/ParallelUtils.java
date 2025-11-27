package org.example.ssestreaming.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class ParallelUtils {
    private final ExecutorService executor;

    public ParallelUtils(ExecutorService executor) {
        this.executor = executor;
    }

    /* =========================================================
     * 1. FIRE-AND-FORGET (KHÔNG CHỜ & KHÔNG CÓ KẾT QUẢ)
     * ========================================================= */

    /**
     * Chạy nhiều Runnable song song, KHÔNG đợi hoàn thành.
     * Thích hợp cho fire-and-forget (log, audit, notify,...).
     */
    public void runParallelVoid(Collection<? extends Runnable> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return;
        }
        for (Runnable job : jobs) {
            if (job != null) {
                executor.submit(job);
            }
        }
    }

    /**
     * Chạy 1 Runnable async, không chờ.
     */
    public void runAsync(Runnable job) {
        if (job == null) return;
        executor.submit(job);
    }


    /**
     * Chạy nhiều Runnable song song và CHỜ tất cả hoàn thành.
     */
    public void runParallelAndWait(Collection<? extends Runnable> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return;
        }

        List<Callable<Void>> callables = new ArrayList<>(jobs.size());
        for (Runnable job : jobs) {
            if (job == null) {
                continue;
            }
            callables.add(() -> {
                job.run();
                return null;
            });
        }

        // invokeAll sẽ chờ đến khi tất cả job chạy xong (hoặc thread hiện tại bị interrupt)
        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Chạy nhiều Runnable song song với timeout.
     * Sau timeout, các task chưa hoàn thành sẽ bị cancel.
     */
    public void runParallelAndWait(Collection<? extends Runnable> jobs, long timeout, TimeUnit unit) throws InterruptedException {
        if (jobs == null || jobs.isEmpty()) {
            return;
        }

        List<Callable<Void>> callables = new ArrayList<>(jobs.size());
        for (Runnable job : jobs) {
            if (job == null) continue;
            callables.add(() -> {
                job.run();
                return null;
            });
        }

        executor.invokeAll(callables, timeout, unit);
    }

    /* =========================================================
     * 3. CHẠY SONG SONG VỚI KẾT QUẢ (CALLABLE / SUPPLIER)
     * ========================================================= */

    /**
     * Chạy nhiều Callable song song và trả về List kết quả T theo đúng thứ tự input.
     * Nếu bất kỳ job nào ném exception, method sẽ ném RuntimeException gói lại.
     */
    public <T> List<T> runParallelWithResult(Collection<? extends Callable<T>> jobs){
        if (jobs == null || jobs.isEmpty()) {
            return List.of();
        }

        List<Future<T>> futures = null;
        try {
            futures = executor.invokeAll(jobs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<T> results = new ArrayList<>(futures.size());
        for (Future<T> f : futures) {
            try {
                results.add(f.get());
            } catch (ExecutionException e) {
                // giữ nguyên cause để dễ debug
                throw new RuntimeException("Parallel task failed", e.getCause());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    /**
     * Sugar: nhận List Supplier<T> thay vì Callable<T>.
     */
    public <T> List<T> runParallelWithResultSuppliers(Collection<? extends Supplier<T>> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return List.of();
        }

        List<Callable<T>> jobs = new ArrayList<>(suppliers.size());
        for (Supplier<T> s : suppliers) {
            if (s == null) continue;
            jobs.add(s::get);
        }
        return runParallelWithResult(jobs);
    }

    /**
     * Chạy nhiều Callable song song với timeout.
     * Task chưa xong trước timeout sẽ bị cancel.
     *
     * Lưu ý: Những task bị cancel nếu gọi get() sẽ ném CancellationException.
     */
    public <T> List<T> runParallelWithResult(Collection<? extends Callable<T>> jobs,
                                             long timeout, TimeUnit unit)
            throws InterruptedException {
        if (jobs == null || jobs.isEmpty()) {
            return List.of();
        }

        List<Future<T>> futures = executor.invokeAll(jobs, timeout, unit);
        List<T> results = new ArrayList<>(futures.size());

        for (Future<T> f : futures) {
            try {
                if (!f.isCancelled()) {
                    results.add(f.get());
                } else {
                    // em có thể add null hoặc bỏ qua tùy chính sách
                    results.add(null);
                }
            } catch (ExecutionException e) {
                throw new RuntimeException("Parallel task failed", e.getCause());
            }
        }
        return results;
    }

    /* =========================================================
     * 4. LẤY KẾT QUẢ ĐẦU TIÊN THÀNH CÔNG (invokeAny)
     * ========================================================= */

    /**
     * Chạy nhiều Callable song song, trả về kết quả của task hoàn thành thành công đầu tiên.
     * Các task còn lại sẽ bị cancel.
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> jobs)
            throws InterruptedException, ExecutionException {
        if (jobs == null || jobs.isEmpty()) {
            throw new IllegalArgumentException("jobs must not be empty");
        }
        return executor.invokeAny(jobs);
    }

    /**
     * Giống invokeAny nhưng có timeout.
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> jobs, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (jobs == null || jobs.isEmpty()) {
            throw new IllegalArgumentException("jobs must not be empty");
        }
        return executor.invokeAny(jobs, timeout, unit);
    }

    /* =========================================================
     * 5. TIỆN ÍCH NHỎ
     * ========================================================= */

    /**
     * Đo thời gian chạy 1 job (ms).
     */
    public long measureMillis(Runnable job) {
        long start = System.currentTimeMillis();
        try {
            job.run();
        } finally {
            return System.currentTimeMillis() - start;
        }
    }

    /**
     * Đo thời gian chạy song song nhiều job (ms), có chờ tất cả hoàn thành.
     */
    public long measureParallelMillis(Collection<? extends Runnable> jobs) throws InterruptedException {
        long start = System.currentTimeMillis();
        runParallelAndWait(jobs);
        return System.currentTimeMillis() - start;
    }
}
