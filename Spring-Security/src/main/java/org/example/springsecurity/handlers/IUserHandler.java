package org.example.springsecurity.handlers;

import org.example.springsecurity.configurations.security.UserDetailsImpl;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterUserRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterUserResponse;

public interface IUserHandler {
    RegisterUserResponse registerUser(RegisterUserRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
