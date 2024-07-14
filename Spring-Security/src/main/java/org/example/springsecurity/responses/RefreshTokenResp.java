package org.example.springsecurity.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RefreshTokenResp {
    private String tokenId;
    private String userId;
    private String token;
    private Instant expiryDate;

}
