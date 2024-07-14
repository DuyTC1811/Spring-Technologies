package org.example.springsecurity.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsecurity.requests.LoginReq;
import org.example.springsecurity.requests.RefreshTokenReq;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.responses.LoginResp;
import org.example.springsecurity.responses.RefreshTokenResp;
import org.example.springsecurity.responses.SignupResp;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public interface IAuthenticationHandler extends LogoutHandler {
    SignupResp signup(SignupReq registerRequest);

    LoginResp login(LoginReq loginRequest);

    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    RefreshTokenResp refreshToken(RefreshTokenReq request);
}
