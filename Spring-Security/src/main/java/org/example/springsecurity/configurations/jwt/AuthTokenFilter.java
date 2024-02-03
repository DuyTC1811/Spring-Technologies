package org.example.springsecurity.configurations.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.springsecurity.configurations.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        var jwtToken = parseJwtToken(request);
        if (jwtUtil.validateJwtToken(jwtToken)) {
            var username = jwtUtil.getUserNameFromJwtToken(jwtToken);
            var userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(filterChain, userDetails);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException exception) {
                LOGGER.error("ServletException IOException [ EXCEPTION ]: => {}", exception.getMessage());
            }
        }

    }

    private String parseJwtToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.startsWith("Bearer ") ? authorization : "";
        }
        return "";
    }
}
