package com.example.springkeycloak.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeycloakCreateUserRequest {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
//    private List<String> roles;
}
