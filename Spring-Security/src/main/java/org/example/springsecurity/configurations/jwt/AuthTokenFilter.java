package org.example.springsecurity.configurations.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.security.UserInfoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserInfoServiceImpl userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        var jwtToken = jwtUtil.parseJwt(request);
        if (jwtUtil.validateJwtToken(jwtToken)) {
            var username = jwtUtil.extractUsername(jwtToken);
            var userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,                  // Principal: thông tin người dùng
                    //TODO                        // Credentials: mật khẩu của người dùng (nếu dùng JWT thì không cần thiết)
                    userDetails.getAuthorities()  // Authorities: danh sách quyền hạn
            );

            // Thiết lập chi tiết xác thực từ yêu cầu HTTP
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Thiết lập Authentication vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException exception) {
                LOGGER.error("[ DO-FILTER-INTERNAL ]: => {}", exception.getMessage());
            }
        }

    }

}
