package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.AuthEntryPointJwt;
import org.example.springsecurity.configurations.jwt.AuthTokenFilter;
import org.example.springsecurity.exceptions.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
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

    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        String[] endpointPermitAll = replaceAll(endpoint);
        try {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)                                  // Disable CSRF protection)
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))      // Enable CORS with custom configuration
                    .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(endpointPermitAll).permitAll()
                            .anyRequest().authenticated())

                    .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);  // JWT filter
            return httpSecurity.build();

        } catch (Exception exception) {
            LOGGER.error("[ ERROR ] Configuring SecurityFilterChain {}", exception.getLocalizedMessage());
            throw new BaseException(400, "Lỗi hệ thông vui lòng thử lại sau");
        }
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String[] replaceAll(String value) {
        return value.replace(" ", "").split(",");
    }

}
