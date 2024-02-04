package org.example.springsecurity.requests;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterRequest {
    @Hidden
    private String userId = UUID.randomUUID().toString();
    @NotNull
    private String username;
    @NotNull
    private String password;
}
