package com.example.springkeycloak.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeycloakUpdateUserRequest {
    private String emailId;
    private String firstname;
    private String lastName;
//    private List<String> roles;
}
