package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.example.springsecurity.configurations.security.UserDetailsImpl;
import org.example.springsecurity.requests.LoginRequest;

@Mapper
public interface ILoginMapper {
    UserDetailsImpl login(LoginRequest loginRequest);
}
