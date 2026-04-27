package org.example.springsecurity.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleRequest {

    @NotBlank(message = "userId bắt buộc không được để trống")
    private String userId;

    @NotBlank(message = "roleId bắt buộc không được để trống")
    private String roleId;
}
