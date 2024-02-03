package org.example.springsecurity.handlers;

import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterResponse;

public interface IServiceUser {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
