package org.example.springsecurity.handlers;

import org.example.springsecurity.responses.TokenResetResp;

public interface ITokenHandler {
    TokenResetResp resetToken(String token);
    String createRefreshToken(String token);
}
