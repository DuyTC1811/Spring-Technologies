package org.example.ssestreaming.service;

import org.example.ssestreaming.common.UploadStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUploadService {
    UploadStatus upLoadFile(MultipartFile file);

    UploadStatus multipleUpload(List<MultipartFile> file);
}
