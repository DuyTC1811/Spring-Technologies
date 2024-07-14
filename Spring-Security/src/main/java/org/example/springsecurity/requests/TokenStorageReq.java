package org.example.springsecurity.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenStorageReq {
    private String tokenId;
    private String assetToken;
    private String refreshToken;
    private String username;
}
