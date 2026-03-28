package org.example.springsecurity.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorSetupResp {
    private String secret;
    private String otpAuthUrl;
    private String qrCodeBase64;
}
