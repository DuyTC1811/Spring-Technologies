package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface IPermissionsMapper {
    Set<String> findPermissionsByUserId(String userId);
}
