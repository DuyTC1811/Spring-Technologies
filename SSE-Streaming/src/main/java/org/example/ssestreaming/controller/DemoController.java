package org.example.ssestreaming.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadService;
import org.example.ssestreaming.common.UploadState;
import org.example.ssestreaming.common.UploadStatus;
import org.example.ssestreaming.service.DemoAsyncService;
import org.example.ssestreaming.service.UploadStreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static org.example.ssestreaming.utils.SseEventUtils.build;

@Slf4j
@RestController
@RequestMapping("/demo")

public class DemoController {
    private final UploadService uploadService;
    private final UploadStreamService uploadServiceStream;
    private final SseProgressStore statusStore;
    private final DemoAsyncService demoAsyncService;
    private final ExecutorService executor;


    public DemoController(UploadService uploadService, UploadStreamService uploadServiceStream, SseProgressStore statusStore, DemoAsyncService demoAsyncService, ExecutorService executor) {
        this.uploadService = uploadService;
        this.uploadServiceStream = uploadServiceStream;
        this.statusStore = statusStore;
        this.demoAsyncService = demoAsyncService;
        this.executor = executor;
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

            try {
                emitter.send(build("upload-progress", status));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (status.getState() == UploadState.SUCCESS || status.getState() == UploadState.FAILED) {
                emitter.complete();
            }

            try {
                Thread.sleep(100); // 1 giây update 1 lần
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
