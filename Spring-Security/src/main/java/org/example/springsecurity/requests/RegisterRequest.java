package org.example.springsecurity.requests;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterRequest {
    @Hidden
    private String userId = UUID.randomUUID().toString();
    private String username;
    private String password;
}
