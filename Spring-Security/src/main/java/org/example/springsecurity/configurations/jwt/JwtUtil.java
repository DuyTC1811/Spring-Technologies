package org.example.springsecurity.configurations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.models.GenerateTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${spring.security.access-token}")
    private String secretAccessToken;
    @Value("${spring.security.access-token-time}")
    private int tokenExpiryTime;

    @Value("${spring.security.refresh-token}")
    private String secretRefreshToken;
    @Value("${spring.security.refresh-token-time}")
    private int refreshExpiryTime;


    public String extractUsername(String token) {
        return extractClaim(token, secretAccessToken, Claims::getSubject);
    }

    public String generateToken(GenerateTokenInfo info) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", info.getEmail());
        claims.put("phone", info.getPhone());
        String assetToken = createToken(claims, info.getUsername(), secretAccessToken, tokenExpiryTime);
        LOGGER.info("[ ACCESS-TOKEN ] - {}", assetToken);
        return assetToken;
    }

    public String refreshToken(String username) {
        Map<String, Object> claims = Map.of("username", username);
        String refreshToken = createToken(claims, username, secretRefreshToken, refreshExpiryTime);
        LOGGER.info("[ REFRESH-TOKEN ] - {}", refreshToken);
        return refreshToken;
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
    private String createToken(Map<String, Object> claims, String subject, String secretKey, long expirationTime) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(generalSigningKey(secretKey))
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
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token, secretAccessToken);
    }

    public boolean isReFreshTokenValid(String token) {
        return isTokenExpired(token, secretRefreshToken);
    }

    private boolean isTokenExpired(String token, String secretKey) {
        return !extractExpiration(token, secretKey).before(new Date());
    }

    private Date extractExpiration(String token, String secretKey) {
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
}
