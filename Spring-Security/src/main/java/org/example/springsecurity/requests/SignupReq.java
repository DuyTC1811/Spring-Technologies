package org.example.springsecurity.requests;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SignupReq {
    @Hidden
    private String uuid;

    @Schema(description = "Tài khoản user", example = "duytc")
    private String username;

    @Schema(description = "Password", example = "duytc")
    private String password;

    @Schema(description = "Số điện thoại", example = "0902288686")
    private String mobile;

    @Schema(description = "Địa chỉ email", example = "duytc@gmail.com")
    private String email;
}
