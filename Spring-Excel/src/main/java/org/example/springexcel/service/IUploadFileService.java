package org.example.springexcel.service;

import org.example.springexcel.model.ResponseData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUploadFileService {
    void uploadFiles(List<MultipartFile> files);

    List<ResponseData> readSingleFile(MultipartFile file);
}
