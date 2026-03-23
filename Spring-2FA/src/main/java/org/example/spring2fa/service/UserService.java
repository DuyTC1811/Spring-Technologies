package org.example.spring2fa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring2fa.config.MissingTOTPKeyAuthenticatorException;
import org.example.spring2fa.config.TOTPAuthenticator;
import org.example.spring2fa.demo.User;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserRepository userRepository;
    private final TOTPAuthenticator totpAuthenticator;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSecret(totpAuthenticator.generateSecret());
        return userRepository.save(user);
    }

    public String generateOtpProtocol(String userName) {
        User user = (User) userRepository.findById(userName).orElse(null);

        String issuer = env.getRequiredProperty("2fa.application.name");
        String account = user.getUsername() + "@domain.com";

        String encodedIssuer = urlEncode(issuer);
        String encodedAccount = urlEncode(account);

        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                encodedIssuer,
                encodedAccount,
                user.getSecret(),
                encodedIssuer
        );
    }

    public String generateQrCode(String otpProtocol) {
        return totpAuthenticator.generateQrCodeBase64(otpProtocol);
    }

    public boolean validateTotp(String userName, Integer totpCode) {
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        String secret = user.getSecret();
        if (!StringUtils.hasText(secret)) {
            return true;
        }

        if (totpCode == null) {
            throw new MissingTOTPKeyAuthenticatorException("TOTP code is mandatory");
        }

        int variance = Integer.parseInt(env.getRequiredProperty("2fa.application.time"));

        try {
            boolean valid = totpAuthenticator.verifyCode(secret, totpCode, variance);
            if (!valid) {
                log.warn("Invalid TOTP code for user={}", userName);
                throw new BadCredentialsException("Invalid TOTP code");
            }
            return true;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("TOTP code verification failed", e);
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
