package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RegisterUserRequest {
    private String userId = UUID.randomUUID().toString();
    private String username;
    private String password;
    private Set<String> rolesIds;
}
