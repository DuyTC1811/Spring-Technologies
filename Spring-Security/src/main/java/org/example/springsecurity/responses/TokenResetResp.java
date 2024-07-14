package org.example.springsecurity.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
public class TokenResetResp {
    private String token;
    private String resetToken;
    private Instant expiryDate;

}
