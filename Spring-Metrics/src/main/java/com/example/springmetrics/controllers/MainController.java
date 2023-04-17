package com.example.springmetrics.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {
    @GetMapping("/get")
    public ResponseEntity<String> getFunction() {
        return ResponseEntity.ok().body("getFunction");
    }

    @PostMapping("/post")
    public ResponseEntity<String> postFunction() {
        return ResponseEntity.ok().body("postFunction");
    }

    @PutMapping("/put")
    public ResponseEntity<String> putFunction() {
        return ResponseEntity.ok().body("putFunction");
    }
}
