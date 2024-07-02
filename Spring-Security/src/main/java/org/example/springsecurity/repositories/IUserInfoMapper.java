package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springsecurity.configurations.security.UserInfo;

@Mapper
public interface IUserInfoMapper {

    @Select("SELECT * FROM users us WHERE us.username = #{username}")
    UserInfo findByUsername(@Param("username") String username);
}
