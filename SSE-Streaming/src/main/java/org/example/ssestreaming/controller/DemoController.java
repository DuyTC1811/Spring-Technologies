package org.example.ssestreaming.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadService;
import org.example.ssestreaming.common.UploadState;
import org.example.ssestreaming.common.UploadStatus;
import org.example.ssestreaming.service.DemoAsyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
    private final UploadService uploadService;
    private final SseProgressStore statusStore;
    private final DemoAsyncService demoAsyncService;


    public DemoController(UploadService uploadService, SseProgressStore statusStore, DemoAsyncService demoAsyncService) {
        this.uploadService = uploadService;
        this.statusStore = statusStore;
        this.demoAsyncService = demoAsyncService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("files") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No files uploaded"));
        }

        List<String> uploadIds = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "One or more files are empty"));
            }

            String uploadId = UUID.randomUUID().toString();
            uploadIds.add(uploadId);

            // phải truyền cả file vào service
            uploadService.processFileAsync(uploadId, file);
        }

        return ResponseEntity.accepted()
                .body(Map.of("uploadIds", uploadIds));
    }


    @GetMapping("/upload/stream")
    public SseEmitter streamUpload(@RequestParam("uploadId") String uploadId) {
        SseEmitter emitter = new SseEmitter(0L);
                while (true) {
                    UploadStatus status = statusStore.get(uploadId);
                    if (status == null) {
                        emitter.complete();
                    }

                    emitter.send(
                            SseEmitter.event()
                                    .id()
                                    .name("upload-progress")
                                    .data(status)
                    );

                    if (status.getState() == UploadState.SUCCESS || status.getState() == UploadState.FAILED) {
                        emitter.complete();
                    }

                    Thread.sleep(100); // 1 giây update 1 lần
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }

        return emitter;
    }

    @GetMapping("/async-demo")
    public String demo() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            demoAsyncService.doWorkAsync("test-" + i);
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        log.info("[ASYNC-DEMO] Triggered 100000 async tasks in {} ms", duration);

        return "Triggered 100000 async tasks in " + duration + " ms";
    }
}
