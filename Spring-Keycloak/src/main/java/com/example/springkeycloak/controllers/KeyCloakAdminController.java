package com.example.springkeycloak.controllers;

import com.example.springkeycloak.dto.request.KeycloakCreateUserRequest;
import com.example.springkeycloak.dto.response.KeycloakRegisterUserResponse;
import com.example.springkeycloak.dto.request.KeycloakResetPasswordRequest;
import com.example.springkeycloak.dto.request.KeycloakUpdateUserRequest;
import com.example.springkeycloak.service.KeyCloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class KeyCloakAdminController {
    private final KeyCloakService keyCloakService;

    public KeyCloakAdminController(KeyCloakService keyCloakService) {
        this.keyCloakService = keyCloakService;
    }

    @PostMapping("/create")
    public ResponseEntity<KeycloakRegisterUserResponse> addUser(@RequestBody KeycloakCreateUserRequest createUser) {
        KeycloakRegisterUserResponse user = keyCloakService.createUser(createUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/list-user")
    public ResponseEntity<List<UserRepresentation>> getUser() {
        List<UserRepresentation> listUser = keyCloakService.listUser();
        return ResponseEntity.ok(listUser);
    }

    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") String userId, @RequestBody KeycloakUpdateUserRequest user) {
        keyCloakService.updateUser(userId, user);
        return ResponseEntity.ok("User Details Updated Successfully.");
    }

    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        keyCloakService.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully.");
    }

    @GetMapping(path = "/verification-link/{userId}")
    public ResponseEntity<String> sendVerificationLink(@PathVariable("userId") String userId) {
        keyCloakService.sendVerificationLink(userId);
        return ResponseEntity.ok("Verification Link Send to Registered E-mail Id.");
    }

    @GetMapping(path = "/reset-password/{userId}")
    public ResponseEntity<String> sendResetPassword(@PathVariable("userId") String userId, @RequestBody KeycloakResetPasswordRequest keycloakResetPasswordRequest) {
        keyCloakService.resetPassword(userId, keycloakResetPasswordRequest);
        return ResponseEntity.ok("Reset Password Link Send Successfully to Registered E-mail Id.");
    }
}
