package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.UploadStatus;
import org.example.ssestreaming.handler.UpLoadHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.example.ssestreaming.enums.UploadState.PROCESSING;


@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements IUploadService {
    private final UpLoadHandler handler;

    @Override
    public UploadStatus upLoadFile(MultipartFile file) {
        String uploadId = java.util.UUID.randomUUID().toString();
        handler.handlerUpload(uploadId, file);
        return new UploadStatus(uploadId, 0, PROCESSING, "IN_PROGRESS");
    }
}
