package org.example.springexcel.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUploadFileService {
    void uploadFiles(List<MultipartFile> files);

    void readSingleFile(List<MultipartFile> files);
}
