package org.example.springsecurity.enums;

import lombok.Getter;

@Getter
public enum EException {
    USER_NOT_FOUND(400, "User not found"),
    OTP_INVALID(400, "Failed to verify TOTP code"),
    OTP_IS_INCORRECT(400, "OTP is incorrect"),
    USER_ALREADY_EXISTS(400, "User already exists");

    private final int code;
    private final String message;
    EException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
