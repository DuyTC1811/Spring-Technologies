package org.example.springsecurity.configurations.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.springsecurity.configurations.caffeine.ICacheService;
import org.example.springsecurity.configurations.security.UserInfoServiceImpl;
import org.example.springsecurity.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    @Value("${spring.security.access-token}")
    private String secretAccessToken;

    private final JwtUtil jwtUtil;
    private final ICacheService cacheService;
    private final UserInfoServiceImpl userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) {
        var jwtToken = jwtUtil.parseJwt(request);
        try {
            if (jwtToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            //TODO Check Black List
            String jti = jwtUtil.extractJti(jwtToken, secretAccessToken);
            String cache = cacheService.getCache(jti);
            if (StringUtils.isNoneBlank(cache)) {
                throw new BaseException(403, "Token của bạn không hợp lệ vui lòng đăng nhập lại");
            }

            var username = jwtUtil.extractUsername(jwtToken, secretAccessToken);
            var tokenVersion = jwtUtil.extractVersion(jwtToken, secretAccessToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(jwtToken, userDetails)) {

                    //TODO check version
                    if (userDetails.getTokenVersion() != tokenVersion) {
                        throw new BaseException(403, "Token của bạn không hợp lệ vui lòng đăng nhập lại");
                    }

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
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

}
