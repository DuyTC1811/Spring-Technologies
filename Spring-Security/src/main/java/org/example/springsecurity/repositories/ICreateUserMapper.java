package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.example.springsecurity.requests.CreateUserRequest;

@Mapper
public interface ICreateUserMapper {
    int registerUser(CreateUserRequest userRequest);
}
