package org.example.springsecurity.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdatePasswordReq {
    @NotBlank(message = "Not Blank ")
    private String newPassword;

    @NotBlank(message = "Not Blank ")
    private String passwordOld;
}
