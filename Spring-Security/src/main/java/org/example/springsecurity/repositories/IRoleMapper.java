package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface IRoleMapper {
    @Select("SELECT ROLE_NAME FROM ROLE WHERE ROLE_NAME = #{roleName}")
    String finByRoleName(String roleName);

    @Select("SELECT SLUG FROM ROLE WHERE ROLE_ID = #{roleId}")
    String finByRoleId(String roleId);

    @Select("SELECT ROLE_ID, SLUG FROM ROLE")
    List<Map<String, Object>> findRoles();
}
