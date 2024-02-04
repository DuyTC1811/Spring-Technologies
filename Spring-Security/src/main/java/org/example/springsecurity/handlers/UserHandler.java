package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.JwtUtil;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.repositories.IRoleMapper;
import org.example.springsecurity.repositories.ITokenMapper;
import org.example.springsecurity.repositories.IUserMapper;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterUserRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.springsecurity.enums.EException.USER_ALREADY_EXISTS;
import static org.example.springsecurity.enums.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {
    private final JwtUtil jwtUtil;
    private final IUserMapper userMapper;
    private final IRoleMapper roleMapper;
    private final ITokenHandler tokenHandler;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterUserResponse registerUser(RegisterUserRequest registerRequest) {
        if (userMapper.isExistsUsername(registerRequest.getUsername())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        if (CollectionUtils.isEmpty(registerRequest.getRoles())) {
            registerRequest.getRoles().add(Set.of(ROLE_USER).toString());
        }
        Set<String> strRoles = registerRequest.getRoles();
        Set<String> roles = new HashSet<>();

        // Lấy toàn bộ roles sau đó map key value
        var stringMap = roleMapper.findRoles().stream()
                .collect(Collectors.toMap(
                        key -> key.get("slug").toString(),
                        value -> value.get("role_id").toString()
                ));

        // Tiếp đó tìm những roles có trong map không có thì thêm id role user vào 0124f4ad-ea1e-47f3-8a23-b53f0c21c90b mã mặc định
        strRoles.forEach(role ->
                roles.add(stringMap.getOrDefault(role, "0124f4ad-ea1e-47f3-8a23-b53f0c21c90b"))
        );
        registerRequest.setRoles(roles);
        userMapper.registerUser(registerRequest);

        // generate Token khi đăng ký thành công
        String token = jwtUtil.generateToken(registerRequest.getUsername(), roles);
        String resetToken = tokenHandler.createRefreshToken(registerRequest.getUserId());
        return new RegisterUserResponse(token, resetToken);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }


}
