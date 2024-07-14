package org.example.springsecurity.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {
    @Schema(description = "username", example = "duytc")
    private String username;

    @Schema(description = "password", example = "duytc")
    private String password;
}
