package org.example.springsecurity.handlers;

import java.util.List;
import java.util.Set;

public interface IRoleHandler {
    Set<String> finByRoleName(Set<String> roleName);

    void insertUserRole(String userId, Set<String> roleId);
}
