package org.example.ssestreaming.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadStatus;
import org.example.ssestreaming.service.IUploadService;
import org.example.ssestreaming.service.SseEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;

@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final IUploadService uploadService;
    private final SseProgressStore statusStore;
    private final ExecutorService executor;
    private final SseEventService service;


    @PostMapping("/upload")
    public ResponseEntity<UploadStatus> upload(@RequestParam("files") MultipartFile files) {
        UploadStatus uploadStatus = uploadService.upLoadFile(files);
        return ResponseEntity.ok(uploadStatus);
    }


    @GetMapping("/upload/stream")
    public SseEmitter stream(@RequestParam("streamId") String streamId) {
       return service.handleUploadProgress(streamId);
    }

}
