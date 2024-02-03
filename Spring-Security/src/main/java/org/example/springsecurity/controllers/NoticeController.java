package org.example.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class NoticeController {
    @GetMapping("/notice")
    public String getNotice() {
        return "This Notice Controller";
    }
}
