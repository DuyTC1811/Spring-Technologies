package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadState;
import org.example.ssestreaming.common.UploadStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class UploadStreamService {
    private final SseProgressStore statusStore;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createUploadStream(String uploadId) throws IOException {
        SseEmitter emitter = new SseEmitter(0L);
        int eventId = 0;
        while (true) {
            UploadStatus status = statusStore.get(uploadId);
            if (status == null) {
                emitter.complete();

            }
            emitter.send(
                    SseEmitter.event()
                            .id(String.valueOf(eventId++))
                            .name("upload-progress")
                            .data(status)
            );

            if (status.getState() == UploadState.SUCCESS || status.getState() == UploadState.FAILED) {
                emitter.complete();
            }

        }
    }

    public SseEmitter subscribe(String uploadId) {

        UploadStatus status = statusStore.get(uploadId);

        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        emitters.add(emitter);

        emitter.onCompletion(
                () -> emitters.remove(emitter)
        );
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            emitters.remove(emitter);
        });
        try {
            emitter.send(SseEmitter.event().name("initialEvent").data("Welcome!"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void sendUpdate(String data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("update")
                        .data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
    }
}
