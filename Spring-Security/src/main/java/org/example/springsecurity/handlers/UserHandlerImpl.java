package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.JwtUtil;
import org.example.springsecurity.exceptions.BaseException;
//import org.example.springsecurity.repositories.IRoleMapper;
//import org.example.springsecurity.repositories.ITokenMapper;
import org.example.springsecurity.repositories.IUserMapper;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.SignupResp;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.example.springsecurity.enums.EException.USER_ALREADY_EXISTS;
import static org.example.springsecurity.enums.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserHandlerImpl implements IUserHandler {
    private final JwtUtil jwtUtil;
    private final IUserMapper userMapper;
//    private final IRoleMapper roleMapper;
//    private final ITokenHandler tokenHandler;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignupResp signup(SignupReq request) {
        if (userMapper.isExistsUsername(request.getUsername())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }

        if (userMapper.isExistsEmail(request.getEmail())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }

        if (userMapper.isExistsMobile(request.getMobile())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }
        request.setUuid(UUID.randomUUID().toString());
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        userMapper.insertUserInfo(request);

        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }


}
