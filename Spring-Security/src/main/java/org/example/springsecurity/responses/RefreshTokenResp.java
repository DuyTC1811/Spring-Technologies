package org.example.springsecurity.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResp {
    private String accessToken;
    private String refreshToken;
}
