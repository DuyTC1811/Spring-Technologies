package com.example.dynamicdatasource.controllers;

import com.example.dynamicdatasource.models.User;
import com.example.dynamicdatasource.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    private final IUserService userService;

    public MainController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/master")
    public ResponseEntity<List<User>> master() {
        return ResponseEntity.ok(userService.getListMaster());
    }

    @GetMapping("/slave")
    public ResponseEntity<List<User>> slave() {
        return ResponseEntity.ok(userService.getListSlave());
    }
}
