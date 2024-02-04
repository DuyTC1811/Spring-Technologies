package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.springsecurity.requests.RegisterUserRequest;

@Mapper
public interface IUserMapper {
    int registerUser(RegisterUserRequest userRequest);

    boolean isExistsUsername(String username);
}
