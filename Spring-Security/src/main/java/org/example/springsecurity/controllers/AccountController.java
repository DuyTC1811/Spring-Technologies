package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IUserHandler;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterUserRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/account")
public class AccountController {
    private final IUserHandler userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest registerRequest) {
        var response = userService.registerUser(registerRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new LoginResponse());
    }

}
