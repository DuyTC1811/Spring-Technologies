package org.example.springsecurity.configurations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.models.GenerateTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public String extractUsername(String token, String secretKey) {
        return extractClaim(token, secretKey, Claims::getSubject);
    }

    public Integer extractVersion(String token, String secretKey) {
        return extractClaim(token, secretKey, claims -> claims.get("version", Integer.class));
    }

    public String extractJti(String token, String secretKey) {
        return extractClaim(token, secretKey, Claims::getId);
    }


    public String accessToken(GenerateTokenInfo info) {
        Map<String, Object> claims = Map.of(
                "username", info.getUsername(),
                "version", info.getVersion()
        );
        String assetToken = createToken(info.getUuid(), claims, info.getUsername(), info.getAccessKey(), info.getAccessExpireTime());
        LOGGER.info("[ ACCESS-TOKEN ] - {}", assetToken);
        return assetToken;
    }

    public String refreshToken(GenerateTokenInfo info) {
        Map<String, Object> claims = Map.of(
                "username", info.getUsername(),
                "version", info.getVersion()
        );
        String refreshToken = createToken(info.getUuid(), claims, info.getUsername(), info.getRefreshKey(), info.getRefreshExpireTime());
        LOGGER.info("[ REFRESH-TOKEN ] - {}", refreshToken);
        return refreshToken;
    }

    public String verifiedToken(GenerateTokenInfo info, String secretKey, int expiryTime) {
        Map<String, Object> claims = Map.of(
                "username", info.getUsername()
        );
        String token = createToken(info.getUuid(), claims, info.getUsername(), secretKey, expiryTime);
        LOGGER.info("[ VERIFIED-TOKEN ] - {}", token);
        return token;
    }


    public <T> T extractClaim(String token, String secretKey, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    /**
     * Phương thức tạo Token
     *
     * @param claims         thông tin cần thêm
     * @param subject        token này là của ai
     * @param expirationTime thời hạn token này có thể tồn tại (millisecond)
     * @return trả về một chuỗi token
     */
    private String createToken(
            String uuid,
            Map<String, Object> claims,
            String subject,
            String secretKey,
            long expirationTime) {
        return Jwts.builder()
                .id(uuid)
                .issuer("http://example.org")
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(generalSigningKey(secretKey))
                .encryptWith(generalSigningKey(secretKey), Jwts.ENC.A256GCM)
                .compact();
    }

    /**
     * Phương thức trích xuất thông tin từ JWT
     *
     * @param token JWT token cần trích xuất claims
     * @return thông tin bên trong JWT
     */
    private Claims extractAllClaims(String token, String secretKey) {
        JwtParserBuilder parserBuilder = Jwts.parser().verifyWith(generalSigningKey(secretKey));
        return parserBuilder.build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Phương thức kiểm tra tính hợp lệ của JWT token
     *
     * @param token       JWT token cần kiểm tra
     * @param userDetails Thông tin chi tiết người dùng để so sánh
     * @return boolean giá trị true nếu token hợp lệ, false nếu không hợp lệ
     */
    public boolean isTokenValid(String token, String secretKey, UserDetails userDetails) {
        final String username = extractUsername(token, secretKey);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token, secretKey);
    }

    public boolean isTokenValid(String token, String secretKey) {
        return isTokenExpired(token, secretKey);
    }

    private boolean isTokenExpired(String token, String secretKey) {
        return !extractExpiration(token, secretKey).before(new Date());
    }

    public Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, secretKey, Claims::getExpiration);
    }

    /**
     * Phương thức trích xuất JWT từ HttpServletRequest
     *
     * @param request HttpServletRequest chứa JWT
     * @return JWT token nếu tìm thấy, null nếu không tìm thấy
     */
    public String parseJwt(HttpServletRequest request) {
        String value = request.getHeader("Authorization");
        if (value != null && value.startsWith("Bearer ")) {
            return value.substring(7);
        }
        return null;
    }

    private SecretKey generalSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (keyBytes.length < 32) { // 32 bytes = 256 bits
            LOGGER.error("The key byte array is too short. It must be at least 256 bits (32 bytes) long. {}", secretKey);
            throw new BaseException(400, "Lỗi hệ thông vui lòng thử lại sau");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UserInfo usernameByContext() {
        UserInfo username = new UserInfo();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            LOGGER.error("Authentication is null");
            return username;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo userDetails) {
            username = userDetails;
        } else {
            // Nếu principal không phải là một instance của UserDetails, ghi log lỗi
            if (principal == null) {
                LOGGER.error("Principal is null");
            } else {
                LOGGER.error("Principal is not an instance of UserDetails, it is an instance of: {}", principal.getClass().getName());
            }
        }
        return username;
    }

}
