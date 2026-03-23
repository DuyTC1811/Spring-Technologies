package org.example.spring2fa.config;

public class MissingTOTPKeyAuthenticatorException extends RuntimeException {

    public MissingTOTPKeyAuthenticatorException(String message) {
        super(message);
    }

    public MissingTOTPKeyAuthenticatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
