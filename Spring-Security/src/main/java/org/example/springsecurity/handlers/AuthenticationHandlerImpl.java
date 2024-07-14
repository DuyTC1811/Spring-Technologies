package org.example.springsecurity.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.JwtUtil;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.models.GenerateTokenInfo;
import org.example.springsecurity.models.TokenStorageInfo;
import org.example.springsecurity.repositories.IAuthenticationMapper;
import org.example.springsecurity.repositories.ITokenStorageMapper;
import org.example.springsecurity.requests.LoginReq;
import org.example.springsecurity.requests.RefreshTokenReq;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.requests.TokenStorageReq;
import org.example.springsecurity.responses.LoginResp;
import org.example.springsecurity.responses.RefreshTokenResp;
import org.example.springsecurity.responses.SignupResp;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static org.example.springsecurity.enums.EException.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AuthenticationHandlerImpl implements IAuthenticationHandler {
    private final JwtUtil jwtUtil;
    private final IAuthenticationMapper authMapper;
    private final ITokenStorageMapper tokenStorageMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignupResp signup(SignupReq request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BaseException(400, "Mật khẩu không khớp");
        }

        if (authMapper.isExistsUsername(request.getUsername())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }

        if (authMapper.isExistsEmail(request.getEmail())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }

        if (authMapper.isExistsMobile(request.getMobile())) {
            throw new BaseException(USER_ALREADY_EXISTS);
        }
        request.setUuid(UUID.randomUUID().toString());
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        authMapper.signup(request);
        return new SignupResp("Register Success");
    }

    @Override
    @Transactional
    public LoginResp login(LoginReq loginReq) {
        UserInfo userInfo = authMapper.findByUsername(loginReq.getUsername());
        if (Objects.isNull(userInfo)) {
            throw new BaseException(400, "tai khoan mat khau khong dung");
        }

        if (!passwordEncoder.matches(loginReq.getPassword(), userInfo.getPassword())) {
            throw new RuntimeException("tai khoan mat khau khong dung");
        }

//        if ("AWAITING".equals(userInfo.getActive())) {
//            throw new RuntimeException("Tai khoan chua duoc kich hoat");
//        }

        GenerateTokenInfo generateTokenInfo = GenerateTokenInfo.builder()
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .phone(userInfo.getMobile())
                .build();
        String accessToken = jwtUtil.generateToken(generateTokenInfo);
        String refreshToken = jwtUtil.refreshToken(userInfo.getUsername());

        TokenStorageReq tokenStorage = TokenStorageReq.builder()
                .tokenId(UUID.randomUUID().toString())
                .assetToken(accessToken)
                .refreshToken(refreshToken)
                .username(userInfo.getUsername())
                .build();
        tokenStorageMapper.insertToken(tokenStorage);
        return new LoginResp(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public RefreshTokenResp refreshToken(RefreshTokenReq request) {
        if (jwtUtil.isReFreshTokenValid(request.getRefreshToken())) {
            TokenStorageInfo tokenInfo = tokenStorageMapper.findReFreshToken(request.getRefreshToken());
            if ("INACTIVE".equals(tokenInfo.getStatus())) {
                throw new BaseException(400, "Đã bị hủy");
            }

            GenerateTokenInfo generateTokenInfo = GenerateTokenInfo.builder()
                    .username(tokenInfo.getUsername())
                    .email(tokenInfo.getEmail())
                    .phone(tokenInfo.getMobile())
                    .build();

            String accessToken = jwtUtil.generateToken(generateTokenInfo);
            tokenStorageMapper.updateReFreshToken(accessToken, request.getRefreshToken(), tokenInfo.getCoutRefresh() + 1);
            return new RefreshTokenResp(accessToken, request.getRefreshToken());
        }
        return null;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setStatus(HttpStatus.OK.value());
        if (authentication != null) {
            authentication.setAuthenticated(false);
            SecurityContextHolder.clearContext();
        }
        String assetToken = jwtUtil.parseJwt(request);
        if (assetToken != null) {
            tokenStorageMapper.killToken(assetToken);
        }
    }
}
