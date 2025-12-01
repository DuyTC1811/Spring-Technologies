package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static org.example.ssestreaming.enums.UploadState.FAILED;
import static org.example.ssestreaming.enums.UploadState.SUCCESS;
import static org.example.ssestreaming.utils.SseEventUtils.build;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventServiceImpl implements SseEventService {
    private final SseProgressStore uploadStore;
    private final ExecutorService executor;

    @Override
    public SseEmitter.SseEventBuilder buildEvent(String name, Object data) {
        return SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(name)
                .data(data);
    }

    @Override
    public void pushEvent(Object data) {
        SseEmitter emitter = new SseEmitter(0L);
        try {
            emitter.send(buildEvent("progress", data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SseEmitter handleUploadProgress(String streamId) {
        SseEmitter emitter = new SseEmitter(0L);

            boolean running = true;

        executor.submit(() -> {
            try {
                while (true) {

                    UploadStatus status = uploadStore.get(streamId);

                    // 1) Không còn status => đóng SSE và break
                    if (status == null) {
                        emitter.complete();
                        break;
                    }

                    // 2) Gửi event SSE
                    emitter.send(build("progress", status));

                    // 3) Hoàn tất upload => đóng SSE và break
                    if (status.getState() == SUCCESS || status.getState() == FAILED) {
                        emitter.complete();
                        break;
                    }

                    Thread.sleep(100);
                }

            } catch (Exception e) {
                log.error("Error while sending SSE for fileId [{}]", streamId, e);
                emitter.completeWithError(e);
                Thread.currentThread().interrupt();
            }
        });
        return emitter;
    }
}
