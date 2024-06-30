package org.example.springsecurity.handlers;

import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.SignupResp;

public interface IUserHandler {
    SignupResp signup(SignupReq registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
