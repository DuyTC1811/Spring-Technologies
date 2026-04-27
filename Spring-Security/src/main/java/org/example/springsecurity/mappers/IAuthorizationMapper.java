package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IAuthorizationMapper {
    boolean existsUserById(@Param("userId") String userId);

    boolean existsRoleById(@Param("roleId") String roleId);

    boolean existsUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    int insertUserRole(@Param("id") String id, @Param("userId") String userId, @Param("roleId") String roleId);

    int deleteUserRole(@Param("userId") String userId, @Param("roleId") String roleId);
}
