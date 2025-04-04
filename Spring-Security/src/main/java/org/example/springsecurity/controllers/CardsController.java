package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class CardsController {

    @GetMapping("/user/card")
    public String getCards() {
        return "This Cards getCards Controller";
    }

    @DeleteMapping("/user/card")
    public String deleteCards() {
        return "This Cards deleteCards Controller";
    }

    @PutMapping("/user/card")
    public String putCards() {
        return "This Cards putCards Controller";
    }
}
