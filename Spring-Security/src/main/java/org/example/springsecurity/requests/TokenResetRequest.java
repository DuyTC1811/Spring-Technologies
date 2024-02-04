package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResetRequest {
    private String resetToken;
}
