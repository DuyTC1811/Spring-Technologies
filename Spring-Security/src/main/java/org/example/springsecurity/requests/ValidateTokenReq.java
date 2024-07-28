package org.example.springsecurity.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ValidateTokenReq {
    @NotBlank(message = "Not Blank ")
    private String token;
}
