package org.example.springsecurity.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserResponse {
    private String token;
    private String resetToken;
}
