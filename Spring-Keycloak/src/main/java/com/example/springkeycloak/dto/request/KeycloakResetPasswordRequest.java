package com.example.springkeycloak.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeycloakResetPasswordRequest {
    private String password;
}
