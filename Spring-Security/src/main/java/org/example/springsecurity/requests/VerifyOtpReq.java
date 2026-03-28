package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpReq {
    private String username;
    private String otp;
}
