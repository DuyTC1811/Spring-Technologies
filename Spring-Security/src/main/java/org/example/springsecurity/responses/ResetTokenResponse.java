package org.example.springsecurity.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResetTokenResponse {
    private String tokenId;
    private String userId;
    private String token;
    private Instant expiryDate;

}
