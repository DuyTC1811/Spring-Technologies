package org.example.springsecurity.requests;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.springsecurity.util.RegexUtil.REGEX_EMAIL;
import static org.example.springsecurity.util.RegexUtil.REGEX_PHONE_NUMBER;
import static org.example.springsecurity.util.RegexUtil.REGEX_USERNAME;


@Getter
@Setter
public class SignupReq {
    @Hidden
    private String uuid;

    @Schema(description = "Tài khoản user", example = "duytc")
    @Pattern(regexp = REGEX_USERNAME, message = "Không chứa ký tự đặc biệt")
    private String username;

    @Schema(description = "Password", example = "duytc")
    private String password;

    @Schema(description = "Confirm Password", example = "duytc")
    private String confirmPassword;

    @Schema(description = "Số điện thoại", example = "0902288686")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = "Số điện thoại không hợp lệ.")
    private String mobile;

    @Schema(description = "Địa chỉ email", example = "duytc@gmail.com")
    @Pattern(regexp = REGEX_EMAIL, message = "Email không hợp lệ")
    private String email;

    private Set<String> roles = new HashSet<>();
}
