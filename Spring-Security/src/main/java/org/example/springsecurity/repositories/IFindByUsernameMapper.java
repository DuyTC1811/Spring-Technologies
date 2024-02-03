package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.example.springsecurity.configurations.UserDetailsImpl;

@Mapper
public interface IFindByUsernameMapper {
    UserDetailsImpl findByUsername(String name);
}
