package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IUserHandler;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.SignupResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHENTICATION & AUTHORIZATION", description = "API XÁC THỰC VÀ PHÂN QUYỀN")
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private final IUserHandler userService;
//    private final ITokenHandler tokenHandler;


    @Operation(summary = "ĐĂNG KÝ TÀI KHOẢN")
    @PostMapping("/signup")
    public ResponseEntity<SignupResp> signup(@Valid @RequestBody SignupReq request) {
        var response = userService.signup(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "ĐĂNG NHẬP TÀI KHOẢN")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(new LoginResponse());
    }

//    @PostMapping("/reset-token")
//    public ResponseEntity<TokenResetResponse> login(@RequestBody TokenResetRequest tokenResetRequest) {
//        TokenResetResponse response = tokenHandler.resetToken(tokenResetRequest.getResetToken());
//        return ResponseEntity.ok().body(response);
//    }

}
