package com.example.springrediscache.controllers;

import com.example.springrediscache.entity.Customer;
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
        Customer customer = Customer
                .builder()
                .id(1)
                .firstName("DuyTC")
                .lastName("Nguyen")
                .email("noname@gmail.com")
                .gender("Male")
                .contactNo("0987654321")
                .country("Vietnam")
                .dob("1999-01-01")
                .build();
        cacheService.setValue("key", customer, Duration.ofHours(1));
        return "Create";
    }

    @GetMapping("/get-cache")
    public Object hello() {
        return cacheService.getValue("key", Customer.class);
    }
}
