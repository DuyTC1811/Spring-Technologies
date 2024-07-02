package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.AuthEntryPointJwt;
import org.example.springsecurity.configurations.jwt.AuthTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)                                  // Disable CSRF protection)
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))      // Enable CORS with custom configuration
                    .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/webjars/**").permitAll()
                            .requestMatchers("api/auth/**").permitAll()
                            .anyRequest().authenticated())

                    .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);  // JWT filter
            return httpSecurity.build();

        } catch (Exception exception) {
            LOGGER.error("[ ERROR ] Configuring SecurityFilterChain {}", exception.getLocalizedMessage());
            return null;
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
