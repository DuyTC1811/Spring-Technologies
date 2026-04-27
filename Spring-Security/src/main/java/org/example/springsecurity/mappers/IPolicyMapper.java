package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springsecurity.models.PolicyRule;

import java.util.List;

@Mapper
public interface IPolicyMapper {
    List<PolicyRule> findRulesByPermissionCode(@Param("permissionCode") String permissionCode);
}
