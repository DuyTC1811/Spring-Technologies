package org.example.springexcel.controller;

import lombok.RequiredArgsConstructor;
import org.example.springexcel.helper.FileExcelUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/demo")
@RestController
@RequiredArgsConstructor
public class DemoController {
    private final FileExcelUtils fileExcelUtils;

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return file.getOriginalFilename();
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
