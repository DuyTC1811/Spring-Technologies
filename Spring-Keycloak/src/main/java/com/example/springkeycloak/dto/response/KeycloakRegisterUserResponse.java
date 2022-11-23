package com.example.springkeycloak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeycloakRegisterUserResponse {
    private String userId;
}
