package org.example.springsecurity.handlers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoleHandler {
    String finByRoleName(String roleName);
}
