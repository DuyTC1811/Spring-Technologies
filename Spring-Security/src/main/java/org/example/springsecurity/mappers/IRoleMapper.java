package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface IRoleMapper {
    Set<String> findByRoleCode(@Param("roleCodes") Set<String> roleCodes);

    void insertUserRole(
            @Param("userId") String userId,
            @Param("roleIds") Set<String> roleIds
    );

    Set<String> findRoleByUserId(@Param("userId") String userId);
}
