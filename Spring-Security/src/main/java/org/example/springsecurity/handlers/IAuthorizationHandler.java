package org.example.springsecurity.handlers;

import org.example.springsecurity.requests.AssignRoleRequest;

public interface IAuthorizationHandler {
    void assignRoleToUser(AssignRoleRequest request);

    void removeRoleFromUser(AssignRoleRequest request);
}
