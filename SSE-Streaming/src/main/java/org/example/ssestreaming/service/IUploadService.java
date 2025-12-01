package org.example.ssestreaming.service;

import org.example.ssestreaming.common.UploadStatus;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    UploadStatus upLoadFile(MultipartFile file);
}
