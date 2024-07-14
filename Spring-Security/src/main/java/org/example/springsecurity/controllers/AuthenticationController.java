package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IAuthenticationHandler;
import org.example.springsecurity.requests.LoginReq;
import org.example.springsecurity.requests.RefreshTokenReq;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.responses.LoginResp;
import org.example.springsecurity.responses.LogoutResp;
import org.example.springsecurity.responses.RefreshTokenResp;
import org.example.springsecurity.responses.SignupResp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHENTICATION & AUTHORIZATION", description = "API XÁC THỰC VÀ PHÂN QUYỀN")
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private final IAuthenticationHandler authentication;


    @Operation(summary = "ĐĂNG KÝ TÀI KHOẢN")
    @PostMapping("/signup")
    public ResponseEntity<SignupResp> signup(@Valid @RequestBody SignupReq request) {
        var response = authentication.signup(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "ĐĂNG NHẬP TÀI KHOẢN")
    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody LoginReq loginReq) {
        LoginResp loginResp = authentication.login(loginReq);
        return ResponseEntity.ok().body(loginResp);
    }

    @Operation(summary = "ĐĂNG XUẤT")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResp> logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        authentication.logout(request, response, auth);
        return ResponseEntity.ok().body(new LogoutResp("Logout Successfully"));
    }

    @PostMapping("/reset-token")
    public ResponseEntity<RefreshTokenResp> refreshToken(@RequestBody RefreshTokenReq request) {
        RefreshTokenResp response = authentication.refreshToken(request);
        return ResponseEntity.ok().body(response);
    }

}
