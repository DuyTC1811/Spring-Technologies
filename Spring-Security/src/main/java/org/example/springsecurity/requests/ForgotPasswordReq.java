package org.example.springsecurity.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ForgotPasswordReq {
    @NotBlank(message = "Not Blank ")
    private String email;
}
