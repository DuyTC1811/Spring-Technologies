package com.example.springexcel.controllers;

import com.example.springexcel.helper.JavaHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    private final JavaHelper javaHelper;

    public MainController(JavaHelper javaHelper) {
        this.javaHelper = javaHelper;
    }

    @GetMapping("/read-file")
    public ResponseEntity<List<Map<Object, Map<Object, Map<Object, Map<Object, Object>>>>>> readFileExcel() throws IOException {
        return ResponseEntity.ok(javaHelper.readExcel());
    }
}
