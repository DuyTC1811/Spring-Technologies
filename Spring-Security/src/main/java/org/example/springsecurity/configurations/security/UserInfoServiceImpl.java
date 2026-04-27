package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.mappers.IAuthenticationMapper;
import org.example.springsecurity.mappers.IRoleMapper;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@NullMarked
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserDetailsService {
    private final IAuthenticationMapper authMapper;
    private final IRoleMapper roleMapper;
    @Override
    public UserInfo loadUserByUsername(String username) {
        UserInfo userInfo = authMapper.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);
        }
        Set<String> roleCodes = roleMapper.findRoleByUserId(userInfo.getUserId());
        userInfo.setRoleCodes(roleCodes);
        return userInfo;
    }
}
