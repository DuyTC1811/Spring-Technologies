package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.IAuthorizationHandler;
import org.example.springsecurity.requests.AssignRoleRequest;
import org.example.springsecurity.responses.AssignRoleResp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHORIZATION", description = "API PHÂN QUYỀN")
public class AuthorizationController {
    private final IAuthorizationHandler authorizationHandler;

    @Operation(summary = "PHÂN QUYỀN CHO USER")
    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssignRoleResp> assignRoleToUser(@Valid @RequestBody AssignRoleRequest request) {
        authorizationHandler.assignRoleToUser(request);
        AssignRoleResp response = new AssignRoleResp("Assign role to user successfully");
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "HỦY QUYỀN CỦA USER")
    @DeleteMapping("/remove-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssignRoleResp> removeRoleFromUser(@Valid @RequestBody AssignRoleRequest request) {
        authorizationHandler.removeRoleFromUser(request);
        AssignRoleResp response = new AssignRoleResp("Remove role from user successfully");
        return ResponseEntity.status(OK).body(response);
    }
}
