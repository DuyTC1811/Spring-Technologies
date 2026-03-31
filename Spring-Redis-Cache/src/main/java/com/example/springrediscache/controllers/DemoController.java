package com.example.springrediscache.controllers;

import com.example.springrediscache.service.IRedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final IRedisCacheService cacheService;

    @PostMapping("/put-cache")
    public String create() {
        cacheService.setValue("key", "duytc", Duration.ofHours(1));
        return "Create";
    }

    @GetMapping("/get-cache")
    public String hello() {
        return cacheService.getValue("key", String.class);
    }
}
