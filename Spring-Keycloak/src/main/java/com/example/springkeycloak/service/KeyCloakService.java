package com.example.springkeycloak.service;

import com.example.springkeycloak.dto.request.KeycloakCreateUserRequest;
import com.example.springkeycloak.dto.response.KeycloakRegisterUserResponse;
import com.example.springkeycloak.dto.request.KeycloakResetPasswordRequest;
import com.example.springkeycloak.dto.request.KeycloakUpdateUserRequest;
import org.keycloak.representations.idm.UserRepresentation;


import java.util.List;

public interface KeyCloakService {
    KeycloakRegisterUserResponse createUser(KeycloakCreateUserRequest user);

    List<UserRepresentation> listUser();

    void updateUser(String userId, KeycloakUpdateUserRequest user);

    void deleteUser(String userId);

    void sendVerificationLink(String userId);

    void resetPassword(String userId, KeycloakResetPasswordRequest keycloakResetPasswordRequest);
}
