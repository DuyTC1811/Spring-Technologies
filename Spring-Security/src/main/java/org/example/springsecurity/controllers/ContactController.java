package org.example.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class ContactController {
    @GetMapping("/contact")
    public String getContact() {
        return "This Contact Controller";
    }
}
