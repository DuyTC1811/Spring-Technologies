package org.example.springsecurity.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IAuthorizationHandler;
import org.example.springsecurity.mappers.IAuthorizationMapper;
import org.example.springsecurity.requests.AssignRoleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationHandlerImpl implements IAuthorizationHandler {
    private final IAuthorizationMapper authorizationMapper;

    @Override
    public void assignRoleToUser(AssignRoleRequest request) {
        boolean userExists = authorizationMapper.existsUserById(request.getUserId());
        if (!userExists) {
            throw new IllegalArgumentException("User not found: " + request.getUserId());
        }

        boolean roleExists = authorizationMapper.existsRoleById(request.getRoleId());
        if (!roleExists) {
            throw new IllegalArgumentException("Role not found: " + request.getRoleId());
        }

        boolean assigned = authorizationMapper.existsUserRole(request.getUserId(), request.getRoleId());

        if (assigned) {
            throw new IllegalArgumentException("User already has this role");
        }

        authorizationMapper.insertUserRole(UUID.randomUUID().toString(), request.getUserId(), request.getRoleId());
    }

    @Override
    public void removeRoleFromUser(AssignRoleRequest request) {
        boolean assigned = authorizationMapper.existsUserRole(request.getUserId(), request.getRoleId());

        if (!assigned) {
            throw new IllegalArgumentException("User does not have this role");
        }
        authorizationMapper.deleteUserRole(request.getUserId(), request.getRoleId());
    }
}
