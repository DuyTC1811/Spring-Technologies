package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.security.UserDetailsImpl;
import org.example.springsecurity.repositories.ILoginMapper;
import org.example.springsecurity.repositories.IRegisterMapper;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHandlerI implements IServiceUser {
    private final ILoginMapper loginMapper;
    private final IRegisterMapper registerMapper;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        int register = registerMapper.register(registerRequest);
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserDetailsImpl userDetails = loginMapper.login(loginRequest);
        return null;
    }
}
