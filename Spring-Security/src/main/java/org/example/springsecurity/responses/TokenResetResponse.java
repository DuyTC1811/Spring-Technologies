package org.example.springsecurity.responses;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
public class TokenResetResponse {
    private String token;
    private String resetToken;
    private Instant expiryDate;

}
