package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IAuthenticationHandler;
import org.example.springsecurity.requests.ForgotPasswordReq;
import org.example.springsecurity.requests.LoginReq;
import org.example.springsecurity.requests.RefreshTokenReq;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.requests.UpdatePasswordReq;
import org.example.springsecurity.requests.ValidateTokenReq;
import org.example.springsecurity.responses.ForgotPasswordResp;
import org.example.springsecurity.responses.LoginResp;
import org.example.springsecurity.responses.LogoutResp;
import org.example.springsecurity.responses.RefreshTokenResp;
import org.example.springsecurity.responses.SignupResp;
import org.example.springsecurity.responses.UpdatePasswordResp;
import org.example.springsecurity.responses.ValidateTokenResp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHENTICATION", description = "API XÁC THỰC")
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private final IAuthenticationHandler authentication;


    @Operation(summary = "ĐĂNG KÝ TÀI KHOẢN")
    @PostMapping("/signup")
    public ResponseEntity<SignupResp> signup(@RequestBody @Valid SignupReq request) {
        var response = authentication.signup(request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "ĐĂNG NHẬP TÀI KHOẢN")
    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody @Valid LoginReq loginReq) {
        LoginResp loginResp = authentication.login(loginReq);
        return ResponseEntity.ok().body(loginResp);
    }

    @Operation(summary = "ĐĂNG XUẤT")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResp> logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        authentication.logout(request, response, auth);
        return ResponseEntity.ok().body(new LogoutResp("Logout Successfully"));
    }

    @Operation(summary = "LẤY LẠI TOKEN MỚI")
    @PutMapping("/reset-token")
    public ResponseEntity<RefreshTokenResp> refreshToken(@RequestBody @Valid RefreshTokenReq request) {
        RefreshTokenResp response = authentication.refreshToken(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "THAY ĐỔI PASSWORD")
    @PutMapping("/update-password")
    public ResponseEntity<UpdatePasswordResp> updatePassword(@RequestBody @Valid UpdatePasswordReq request) {
        UpdatePasswordResp response = authentication.updatePassword(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "KIỂM TRA TOKEN")
    @PostMapping("/validate-token")
    public ResponseEntity<ValidateTokenResp> validateToken(@RequestBody @Valid ValidateTokenReq request) {
        ValidateTokenResp resp = authentication.validateToken(request);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "LẤY LẠI PASSWORD")
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResp> forgotPassword(@RequestBody @Valid ForgotPasswordReq request, HttpServletRequest httpRequest) {
        ForgotPasswordResp resp = authentication.forgotPassword(request, httpRequest);
        return ResponseEntity.ok().body(resp);
    }

}
