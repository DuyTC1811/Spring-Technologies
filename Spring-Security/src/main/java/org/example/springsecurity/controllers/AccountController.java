package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.ITokenHandler;
import org.example.springsecurity.handlers.IUserHandler;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterUserRequest;
import org.example.springsecurity.requests.TokenResetRequest;
import org.example.springsecurity.responses.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/account")
public class AccountController {
    private final IUserHandler userService;
    private final ITokenHandler tokenHandler;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest registerRequest) {
        var response = userService.registerUser(registerRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/reset-token")
    public ResponseEntity<TokenResetResponse> login(@RequestBody TokenResetRequest tokenResetRequest) {
        TokenResetResponse response = tokenHandler.resetToken(tokenResetRequest.getResetToken());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new LoginResponse());
    }

}
