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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserInfoServiceImpl userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        var jwtToken = jwtUtil.parseJwt(request);
        try {
            if (jwtToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            var username = jwtUtil.extractUsername(jwtToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,                  // Principal: thông tin người dùng
                            null,                         // Credentials: mật khẩu của người dùng (nếu dùng JWT thì không cần thiết)
                            userDetails.getAuthorities()  // Authorities: danh sách quyền hạn
                    );
                    // Thiết lập chi tiết xác thực từ yêu cầu HTTP
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Thiết lập Authentication vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (IOException | ServletException exception) {
            LOGGER.error("[ DO-FILTER-INTERNAL ]: => {}", exception.getCause().getCause().getMessage());
        }
    }

}
