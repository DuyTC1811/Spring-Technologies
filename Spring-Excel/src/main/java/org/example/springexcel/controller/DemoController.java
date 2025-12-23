package org.example.springexcel.controller;

import lombok.RequiredArgsConstructor;
import org.example.springexcel.helper.FileExcelUtils;
import org.example.springexcel.model.ResponseData;
import org.example.springexcel.service.IUploadFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/demo/upload")
@RestController
@RequiredArgsConstructor
public class DemoController {
    private final FileExcelUtils fileExcelUtils;
    private final IUploadFileService uploadFileService;

    @PostMapping("/upload-file")
    public ResponseEntity<List<ResponseData>> uploadFile(@RequestParam("file") MultipartFile file) {
        List<ResponseData> singleFile = uploadFileService.readSingleFile(file);
        return ResponseEntity.ok(singleFile);
    }

    @PostMapping("/multi-file")
    public String uploadData(@RequestParam("files") List<MultipartFile> files) {
        uploadFileService.uploadFiles(files);
        return "Number of files uploaded: " + files.size();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTemplate() {
        byte[] bytes = fileExcelUtils.exportTemplateBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
}
