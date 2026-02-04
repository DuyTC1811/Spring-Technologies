package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.ssestreaming.enums.UploadState.FAILED;
import static org.example.ssestreaming.enums.UploadState.SUCCESS;
import static org.example.ssestreaming.utils.SseEventUtils.build;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventServiceImpl implements SseEventService {
    private final SseProgressStore uploadStore;
    private final ExecutorService executor;

    @Value("${sse.timeout:1800000}") // 30-minute default
    private Long sseTimeout;

    @Value("${sse.poll.interval:500}") // 500ms default
    private Long pollInterval;

    @Override
    public SseEmitter.SseEventBuilder buildEvent(String name, Object data) {
        return SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(name)
                .data(data);
    }

    /**
     * Send event to an existing emitter
     */
    @Override
    public void pushEvent(SseEmitter emitter, Object data) {
        if (emitter == null) {
            log.warn("Cannot push event - emitter is null");
            return;
        }

        try {
            emitter.send(buildEvent("progress", data));
        } catch (IOException e) {
            log.error("Failed to send SSE event", e);
            emitter.completeWithError(e);
        }
    }

    @Override
    public SseEmitter handleUploadProgress(String streamId) {
        SseEmitter emitter = new SseEmitter(sseTimeout);
        AtomicBoolean shouldStop = new AtomicBoolean(false);

        // Setup cleanup callbacks
        emitter.onCompletion(() -> {
            log.info("SSE connection completed for streamId: {}", streamId);
            shouldStop.set(true);
        });

        emitter.onTimeout(() -> {
            log.warn("SSE connection timed out for streamId: {}", streamId);
            shouldStop.set(true);
        });

        emitter.onError((ex) -> {
            log.error("SSE connection error for streamId: {}", streamId, ex);
            shouldStop.set(true);
        });

        // Submit async task
        Future<?> future = executor.submit(() -> {
            try {
                while (!shouldStop.get()) {
                    UploadStatus status = uploadStore.get(streamId);

                    // No status found - complete and exit
                    if (status == null) {
                        log.debug("No status found for streamId: {}", streamId);
                        emitter.complete();
                        break;
                    }

                    // Send progress event
                    try {
                        emitter.send(build("progress", status));
                    } catch (IOException e) {
                        log.warn("Client disconnected for streamId: {}", streamId);
                        emitter.completeWithError(e);
                        break;
                    }

                    // Upload finished - complete and exit
                    if (status.getState() == SUCCESS || status.getState() == FAILED) {
                        log.info("Upload completed with state {} for streamId: {}", status.getState(), streamId);
                        emitter.complete();
                        break;
                    }

                    Thread.sleep(pollInterval);
                }
            } catch (InterruptedException e) {
                log.warn("Thread interrupted for streamId: {}", streamId);
                Thread.currentThread().interrupt();
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("Unexpected error while streaming progress for streamId: {}", streamId, e);
                emitter.completeWithError(e);
            }
        });

        // Cancel future if the connection closes
        emitter.onCompletion(() -> future.cancel(true));
        emitter.onTimeout(() -> future.cancel(true));

        return emitter;
    }
}
