package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.repositories.providers.UserSqlProvider;

@Mapper
public interface IUserInfoMapper {
    @SelectProvider(type = UserSqlProvider.class, method = "selectUsers")
    UserInfo findByUsername(String name);
}
