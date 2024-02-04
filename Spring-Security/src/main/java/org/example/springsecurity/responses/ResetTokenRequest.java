package org.example.springsecurity.responses;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ResetTokenRequest {
    private String tokenId = UUID.randomUUID().toString();
    private String userId;
    private String token;
    private Instant expiryDate;
}
