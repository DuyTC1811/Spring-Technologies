package org.example.spring2fa.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.InMemoryOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMultiFactorAuthentication(authorities = {
        FactorGrantedAuthority.PASSWORD_AUTHORITY,
        FactorGrantedAuthority.OTT_AUTHORITY
})
public class SecurityConfig {
    /**
     * MFA global: mọi rule authorize sẽ bị “gắn thêm” yêu cầu 2 factors (PASSWORD + OTT)
     * theo đúng cơ chế FactorGrantedAuthority / EnableMultiFactorAuthentication trong docs.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpServletRequest request, org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .oneTimeTokenLogin(withDefaults()); // bật OTT login :contentReference[oaicite:5]{index=5}

        return http.build();
    }

    /**
     * OTT cần OneTimeTokenService để generate/consume token.
     * Demo dùng InMemoryOneTimeTokenService.
     */
    @Bean
    OneTimeTokenService oneTimeTokenService() {
        return new InMemoryOneTimeTokenService();
    }

    /**
     * OTT “bắt buộc” phải có OneTimeTokenGenerationSuccessHandler (hoặc đăng ký @Bean).
     * Demo: tạo magic link và log ra console, rồi redirect sang /ott/sent.
     */
    @Bean
    @NullMarked
    OneTimeTokenGenerationSuccessHandler oneTimeTokenGenerationSuccessHandler() {
        RedirectOneTimeTokenGenerationSuccessHandler redirect = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");

        return new OneTimeTokenGenerationSuccessHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException {

                String magicLink = UriComponentsBuilder
                        .fromUri(URI.create(request.getRequestURL().toString()))
                        .replacePath(request.getContextPath())
                        .replaceQuery(null)
                        .fragment(null)
                        .path("/login/ott")
                        .queryParam("token", oneTimeToken.getTokenValue())
                        .toUriString();

                System.out.println("=== OTT GENERATED ===");
                System.out.println("username: " + oneTimeToken.getUsername());
                System.out.println("magic link: " + magicLink);
                System.out.println("=====================");

                // chuyển user sang trang “đã gửi token”
                redirect.handle(request, response, oneTimeToken);
            }
        };
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // 2 users demo
        var user = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        var admin = User.withUsername("admin")
                .password(encoder.encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
