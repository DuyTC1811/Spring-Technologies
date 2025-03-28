package org.example.springsecurity.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IRoleHandler;
import org.example.springsecurity.repositories.IRoleMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleHandlerImpl implements IRoleHandler {
    private final IRoleMapper roleMapper;

    @Override
    public Set<String> findByRoleCode(Set<String> roleName) {
        return roleMapper.findByRoleCode(roleName);
    }

    @Override
    public void insertUserRole(String userId, Set<String> roleId) {
        roleMapper.insertUserRole(userId, roleId);
    }


}
