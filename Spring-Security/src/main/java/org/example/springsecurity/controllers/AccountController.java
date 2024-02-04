package org.example.springsecurity.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IServiceUser;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.registry.Registry;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/account")
public class AccountController {
    private final IServiceUser userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        var response = userService.register(registerRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new LoginResponse());
    }

}
