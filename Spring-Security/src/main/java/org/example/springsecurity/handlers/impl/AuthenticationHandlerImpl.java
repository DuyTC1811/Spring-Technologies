package org.example.springsecurity.handlers.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.caffeine.ICacheService;
import org.example.springsecurity.configurations.jwt.JwtUtil;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.configurations.security.UserInfoServiceImpl;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.handlers.IAuthenticationHandler;
import org.example.springsecurity.handlers.IRoleHandler;
import org.example.springsecurity.models.GenerateTokenInfo;
import org.example.springsecurity.repositories.IAuthenticationMapper;
import org.example.springsecurity.repositories.ITokenStorageMapper;
import org.example.springsecurity.requests.ForgotPasswordReq;
import org.example.springsecurity.requests.LoginReq;
import org.example.springsecurity.requests.RefreshTokenReq;
import org.example.springsecurity.requests.SignupReq;
import org.example.springsecurity.requests.UpdatePasswordReq;
import org.example.springsecurity.requests.ValidateTokenReq;
import org.example.springsecurity.responses.ForgotPasswordResp;
import org.example.springsecurity.responses.LoginResp;
import org.example.springsecurity.responses.RefreshTokenResp;
import org.example.springsecurity.responses.SignupResp;
import org.example.springsecurity.responses.UpdatePasswordResp;
import org.example.springsecurity.responses.ValidateTokenResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.example.springsecurity.enums.EException.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AuthenticationHandlerImpl implements IAuthenticationHandler {
    @Value("${spring.security.access-token}")
    private String accessSecretToken;
    @Value("${spring.security.access-token-time}")
    private int accessExpiryTime;

    @Value("${spring.security.refresh-token}")
    private String refreshSecretToken;
    @Value("${spring.security.refresh-token-time}")
    private int refreshExpiryTime;

    @Value("${spring.security.verified-token}")
    private String verifiedToken;
    @Value("${spring.security.verified-token-time}")
    private int verifiedTokenTime;

    private final JwtUtil jwtUtil;
    private final IRoleHandler roleHandler;
    private final ICacheService cacheService;
    private final IAuthenticationMapper authMapper;
    private final ITokenStorageMapper tokenStorageMapper;
    private final UserInfoServiceImpl userDetailsService;

    // private final IMailService mailService;
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
        String userId = UUID.randomUUID().toString();
        request.setUuid(userId);
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        authMapper.signup(request);
        Set<String> roleIds = roleHandler.findByRoleCode(Set.of("USER"));
        roleHandler.insertUserRole(userId, roleIds);
        // TODO send Email
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
            throw new BaseException(400, "tai khoan mat khau khong dung");
        }

//        if ("AWAITING".equals(userInfo.getActive())) {
//            throw new RuntimeException("Tai khoan chua duoc kich hoat");
//        }

        GenerateTokenInfo generateTokenInfo = GenerateTokenInfo.builder()
                .uuid(UUID.randomUUID().toString())
                .username(userInfo.getUsername())
                .accessKey(accessSecretToken)
                .accessExpireTime(accessExpiryTime)
                .refreshKey(refreshSecretToken)
                .refreshExpireTime(refreshExpiryTime)
                .version(userInfo.getTokenVersion())
                .build();

        String accessToken = jwtUtil.accessToken(generateTokenInfo);
        String refreshToken = jwtUtil.refreshToken(generateTokenInfo);
        return new LoginResp(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public RefreshTokenResp refreshToken(RefreshTokenReq request) {
        if (jwtUtil.isTokenValid(request.getRefreshToken(), refreshSecretToken)) {
            throw new BaseException(403, "Token của bạn không hợp lệ vui lòng đăng nhập lại");
        }

        Integer version = jwtUtil.extractVersion(request.getRefreshToken(), refreshSecretToken);
        String username = jwtUtil.extractUsername(request.getRefreshToken(), refreshSecretToken);
        var userDetails = userDetailsService.loadUserByUsername(username);

        if (version != userDetails.getTokenVersion()) {
            throw new BaseException(403, "Token của bạn không hợp lệ vui lòng đăng nhập lại");
        }

        GenerateTokenInfo generateTokenInfo = GenerateTokenInfo.builder()
                .uuid(UUID.randomUUID().toString())
                .username(userDetails.getUsername())
                .accessKey(accessSecretToken)
                .accessExpireTime(accessExpiryTime)
                .refreshKey(refreshSecretToken)
                .refreshExpireTime(refreshExpiryTime)
                .version(userDetails.getTokenVersion())
                .build();

        String accessToken = jwtUtil.accessToken(generateTokenInfo);
        String refreshToken = jwtUtil.refreshToken(generateTokenInfo);

        String jti = jwtUtil.extractJti(request.getRefreshToken(), refreshSecretToken);
        String tokenKey = "blacklist:" + jti.hashCode();
        Date expiration = jwtUtil.extractExpiration(request.getRefreshToken(), refreshSecretToken);
        // Add token to blacklist
        cacheService.putCache(jti, tokenKey, expiration.getTime());
        return new RefreshTokenResp(accessToken, refreshToken);
    }

    @Override
    public UpdatePasswordResp updatePassword(UpdatePasswordReq request) {
        UserInfo userInfo = jwtUtil.usernameByContext();
        String passwordOld = authMapper.findPasswordByUserName(userInfo.getUsername());
        if (!passwordEncoder.matches(request.getPasswordOld(), passwordOld) || passwordOld == null) {
            throw new BaseException(400, "tai khoan mat khau khong dung");
        }

        authMapper.updatePassword(userInfo.getUsername(), passwordEncoder.encode(request.getNewPassword()));
        // Update Version Token
        tokenStorageMapper.updateTokenVersion(userInfo.getUsername());
        return new UpdatePasswordResp("Successfully updated password");
    }

    @Override
    public ValidateTokenResp validateToken(ValidateTokenReq request) {
        if (!jwtUtil.isTokenValid(request.getToken(), verifiedToken)) {
            throw new BaseException(403, "Token invalid");
        }
        return new ValidateTokenResp("Successfully validated token");
    }

    @Override
    public ForgotPasswordResp forgotPassword(ForgotPasswordReq request, HttpServletRequest httpRequest) {
        UserInfo userInfo = authMapper.findByUsername(request.getEmail());
        if (Objects.isNull(userInfo)) {
            throw new BaseException(400, "tài khoản của bạn không tồn tại");
        }
        GenerateTokenInfo tokenInfo = GenerateTokenInfo.builder()
                .uuid(UUID.randomUUID().toString())
                .username(userInfo.getUsername())
                .build();
        String generateToken = jwtUtil.verifiedToken(tokenInfo, verifiedToken, verifiedTokenTime);

        // TODO send mail
        // mailService.sendMailForgotPassword(generateToken, userInfo.getEmail());
        return new ForgotPasswordResp("Successfully forgot password");
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setStatus(HttpStatus.OK.value());
        if (authentication != null) {
            authentication.setAuthenticated(false);
            SecurityContextHolder.clearContext();
        }
        String assetToken = jwtUtil.parseJwt(request);
        String jti = jwtUtil.extractJti(assetToken, accessSecretToken);
        String tokenKey = "blacklist:" + jti.hashCode();
        Date expiration = jwtUtil.extractExpiration(assetToken, accessSecretToken);
        // Add token to blacklist
        cacheService.putCache(jti, tokenKey, expiration.getTime());
    }
}
