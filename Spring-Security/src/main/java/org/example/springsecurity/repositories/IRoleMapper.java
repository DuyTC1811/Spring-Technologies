package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface IRoleMapper {
    @Select("SELECT ROLE_ID FROM ROLE WHERE ROLE_CODE IN #{roleCode}")
    Set<String> finByRoleName(Set<String> roleCode);

//    @Select("SELECT SLUG FROM USER_ROLE U LEFT JOIN ROLE R ON U.ROLE_ID = R.ROLE_ID WHERE U.USER_ID = #{userId}")
//    Set<String> finSlugByRoleId(String userId);
//
//    @Select("SELECT ROLE_ID, SLUG FROM ROLE")
//    List<Map<String, Object>> findRoles();
}
