package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.repositories.IAuthenticationMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserDetailsService {
    private final IAuthenticationMapper authMapper;
    @Override
    public UserInfo loadUserByUsername(String username) {
        UserInfo userInfo = authMapper.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);
        }
        return userInfo;
    }
}
