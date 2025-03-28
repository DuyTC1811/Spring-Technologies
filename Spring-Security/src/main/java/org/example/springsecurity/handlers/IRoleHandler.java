package org.example.springsecurity.handlers;

import java.util.Set;

public interface IRoleHandler {
    Set<String> findByRoleCode(Set<String> roleName);

    void insertUserRole(String userId, Set<String> roleId);
}
