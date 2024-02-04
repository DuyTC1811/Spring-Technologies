package org.example.springsecurity.handlers;

import org.example.springsecurity.responses.TokenResetResponse;

public interface ITokenHandler {
    TokenResetResponse resetToken(String token);
    String createRefreshToken(String token);
}
