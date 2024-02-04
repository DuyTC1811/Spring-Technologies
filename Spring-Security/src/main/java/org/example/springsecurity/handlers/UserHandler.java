package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.repositories.ILoginMapper;
import org.example.springsecurity.repositories.IRoleMapper;
import org.example.springsecurity.repositories.IUserMapper;
import org.example.springsecurity.requests.LoginRequest;
import org.example.springsecurity.requests.RegisterUserRequest;
import org.example.springsecurity.responses.LoginResponse;
import org.example.springsecurity.responses.RegisterUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.springsecurity.enums.EException.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {
    private final IUserMapper userMapper;
    private final IRoleMapper roleMapper;
    private final ILoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest registerRequest) {
        if (userMapper.isExistsUsername(registerRequest.getUsername())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Set<String> strRoles = registerRequest.getRolesIds();
        Set<String> roles = new HashSet<>();

        // Lấy toàn bộ roles sau đó map key value
        Map<String, String> stringMap = roleMapper.findRoles().stream()
                .collect(Collectors.toMap(
                        key -> key.get("slug").toString(),
                        value -> value.get("role_id").toString()
                ));

        // Tiếp đó tìm những roles có trong map không có thì thêm id role user vào 0124f4ad-ea1e-47f3-8a23-b53f0c21c90b mã mặc định
        strRoles.forEach(role -> roles.add(stringMap.getOrDefault(role, "0124f4ad-ea1e-47f3-8a23-b53f0c21c90b")));
        registerRequest.setRolesIds(roles);
        userMapper.registerUser(registerRequest);
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }
}
