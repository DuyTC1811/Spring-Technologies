package com.example.springsso.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Value("${cors.allowed.methods}")
    private String allowedMethods;

    @Value("${cors.allowed.headers}")
    private String allowedHeaders;

    @Value("${cors.permit-all.endpoint}")
    private String endpoint;

    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String[] endpointPermitAll = replaceAll(endpoint);

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)                                  // Disable CSRF protection
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))      // Enable CORS with custom configuration
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(endpointPermitAll).permitAll()
                        .anyRequest().authenticated()
                )

                // Config OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))
                );

                // Handling authentication
//                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))

                // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
//                .addFilterBefore(null, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(replaceAll(allowedOrigins)));
        config.setAllowedMethods(List.of(replaceAll(allowedMethods)));
        config.setAllowedHeaders(List.of(replaceAll(allowedHeaders)));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    public String[] replaceAll(String value) {
        return value.replace(" ", "").split(",");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
