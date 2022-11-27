package com.example.springminio.services;

import com.example.springminio.payload.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MinioServices {
    void uploadFiles(MultipartFile[] multipartFile);

    void deleteFile(String fileName);

    List<FileResponse> getListFile();

    byte[] downloadFile(String fileName);

    void deleteFiles(List<String> listFileName);
}
