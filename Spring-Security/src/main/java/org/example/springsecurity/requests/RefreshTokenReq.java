package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RefreshTokenReq {
    private String tokenId;
    private String userId;
    private String token;
    private Instant expiryDate;

}
