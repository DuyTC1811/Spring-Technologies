package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.jwt.AuthEntryPointJwt;
import org.example.springsecurity.configurations.jwt.AuthTokenFilter;
import org.example.springsecurity.configurations.properties.CorsProperties;
import org.example.springsecurity.exceptions.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    private final CorsProperties corsProperties;

    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        String[] allowed = corsProperties.getPermitAll().getEndpoint();
        try {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)                                  // Disable CSRF protection)
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))      // Enable CORS with custom configuration
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(allowed).permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/user/**").hasAnyRole("USER")
                            .requestMatchers("/api/technique/**").hasAnyRole("TECHNIQUE")
                            .requestMatchers("/api/staff/**").hasAnyRole("STAFF")
                            .anyRequest().authenticated())


                    .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))
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
        config.setAllowedOrigins(corsProperties.getAllowed().getOrigins());
        config.setAllowedMethods(corsProperties.getAllowed().getMethods());
        config.setAllowedHeaders(corsProperties.getAllowed().getHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
