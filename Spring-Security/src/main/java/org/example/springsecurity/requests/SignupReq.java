package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupReq {
    private String username;
    private String password;
    private String mobile;
    private String email;
}
