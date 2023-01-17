package com.example.springsendemailsms.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/home")
    public String welcome(Model model) {
        log.info("Spring Boot Thymeleaf Configuration Example");
        String message = "Hello World DUY.T.C";
        model.addAttribute("message", message);
        return "index";
    }
}
