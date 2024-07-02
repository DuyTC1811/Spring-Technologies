package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.repositories.IUserInfoMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserDetailsService {
    private final IUserInfoMapper userInfoMapper;
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo userInfo = userInfoMapper.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);
        }
        return userInfo;
    }
}
