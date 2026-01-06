package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.UploadStatus;
import org.example.ssestreaming.handler.UpLoadHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.example.ssestreaming.enums.UploadState.PROCESSING;
import static org.example.ssestreaming.utils.FileUtils.convertToFile;
import static org.example.ssestreaming.utils.FileUtils.fileToBase64;


@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements IUploadService {
    private final UpLoadHandler handler;
    private final IRedisService redisService;

    @Override
    public UploadStatus upLoadFile(MultipartFile file) {
        String uploadId = java.util.UUID.randomUUID().toString();
        handler.handlerUpload(uploadId, file);
        return new UploadStatus(uploadId, 0, PROCESSING, "IN_PROGRESS");
    }

    @Override
    public UploadStatus multipleUpload(List<MultipartFile> file) {
        for (MultipartFile file1 : file) {
            File convertToFile = convertToFile(file1);
            redisService.setValue("DUYTC", convertToFile, Duration.ofHours(1));
            log.info("multiple upload file: {}", convertToFile);
        }
        File duytc = redisService.getValue("DUYTC", File.class);
        log.info("multiple upload file: {}", duytc);
        String string = fileToBase64(duytc);
        log.info("multiple upload file: {}", string);
        return null;
    }
}
