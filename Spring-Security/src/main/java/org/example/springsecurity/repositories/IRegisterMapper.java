package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.example.springsecurity.requests.RegisterRequest;

@Mapper
public interface IRegisterMapper {
    int register(RegisterRequest registerRequest);
}
