package org.example.ssestreaming.controller;

import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadService;
import org.example.ssestreaming.common.UploadState;
import org.example.ssestreaming.common.UploadStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private final UploadService uploadService;
    private final SseProgressStore statusStore;


    public DemoController(UploadService uploadService, SseProgressStore statusStore) {
        this.uploadService = uploadService;
        this.statusStore = statusStore;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload() {
        String uploadId = UUID.randomUUID().toString();
        uploadService.processFileAsync(uploadId);
        // trả về uploadId cho FE để nó mở SSE
        return ResponseEntity.accepted()
                .body(Map.of("uploadId", uploadId));
    }

    @GetMapping("/upload/stream")
    public SseEmitter streamUpload(@RequestParam("uploadId") String uploadId) {
        SseEmitter emitter = new SseEmitter(0L);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        executor.execute(() -> {
            try {
                int eventId = 0;
                while (true) {
                    UploadStatus status = statusStore.get(uploadId);
                    if (status == null) {
                        emitter.complete();
                        return;
                    }

                    emitter.send(
                            SseEmitter.event()
                                    .id(String.valueOf(eventId++))
                                    .name("upload-progress")
                                    .data(status)
                    );

                    if (status.getState() == UploadState.SUCCESS || status.getState() == UploadState.FAILED) {
                        emitter.complete();
                        return;
                    }

                    Thread.sleep(30000); // 1 giây update 1 lần
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
