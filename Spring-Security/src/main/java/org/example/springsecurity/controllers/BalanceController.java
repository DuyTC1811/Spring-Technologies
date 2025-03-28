package org.example.springsecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class BalanceController {
    @GetMapping("/admin/balance")
    public String getBalance() {
        return "This Balance Controller";
    }
}
