package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.JwtUtil;
import org.example.springsecurity.configurations.security.UserDetailsImpl;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.repositories.IRoleMapper;
import org.example.springsecurity.repositories.ITokenMapper;
import org.example.springsecurity.repositories.IUserMapper;
import org.example.springsecurity.responses.ResetTokenRequest;
import org.example.springsecurity.responses.ResetTokenResponse;
import org.example.springsecurity.responses.TokenResetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenHandler implements ITokenHandler {
    @Value("${spring.security.expiration-time}")
    private long refreshTokenTimeout;
    private final JwtUtil jwtUtil;
    private final ITokenMapper tokenMapper;
    private final IUserMapper userMapper;
    private final IRoleMapper roleMapper;

    @Override
    public TokenResetResponse resetToken(String token) {
        ResetTokenResponse byToken = tokenMapper.findByToken(token);
        verifyExpiration(byToken);
        UserDetailsImpl userDetails = userMapper.findByUserId(byToken.getUserId());
        Set<String> roles = roleMapper.finSlugByRoleId(userDetails.getUserId());
        String newToken = jwtUtil.generateToken(userDetails.getUsername(), roles);
        return new TokenResetResponse(newToken, token, byToken.getExpiryDate());
    }

    @Override
    @Transactional
    public String createRefreshToken(String userId) {
        ResetTokenRequest refreshToken = new ResetTokenRequest();
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenTimeout));
        refreshToken.setToken(UUID.randomUUID().toString());
        tokenMapper.saveToken(refreshToken);
        return refreshToken.getToken();
    }

    private void verifyExpiration(ResetTokenResponse token) {
        if (token == null) {
            throw new BaseException(400, " Ma Token Khong Ton Tai");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenMapper.deleteToken(token.getToken());
            throw new BaseException(400, "Refresh token was expired. Please make a new signin request");
        }
    }
}
