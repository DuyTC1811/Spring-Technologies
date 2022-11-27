package com.example.springminio.controllers;

import com.example.springminio.payload.FileResponse;
import com.example.springminio.services.MinioServices;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/file")
public class MinioController {
    private final MinioServices minioServices;

    public MinioController(MinioServices minioServices) {
        this.minioServices = minioServices;
    }

    @PostMapping(name = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestPart("file") MultipartFile[] multipartFile) {
        minioServices.uploadFiles(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body("successful");
    }

    @GetMapping(name = "/list-file")
    public ResponseEntity<List<FileResponse>> listFile() {
        List<FileResponse> fileList = minioServices.getListFile();
        return ResponseEntity.ok().body(fileList);
    }

    @GetMapping("/download-files/{file-name}")
    public ResponseEntity<ByteArrayResource> downloadFiles(@PathVariable("file-name") String fileName) {
        byte[] response = minioServices.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(response);
        return ResponseEntity.ok()
                .contentLength(response.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + fileName)
                .body(resource);
    }

    @DeleteMapping("/delete-file/{file-name}")
    private ResponseEntity<String> deleteFiles(@PathVariable("file-name") List<String> listFileName) {
        minioServices.deleteFiles(listFileName);
        return ResponseEntity.ok().body("successful");
    }
}
