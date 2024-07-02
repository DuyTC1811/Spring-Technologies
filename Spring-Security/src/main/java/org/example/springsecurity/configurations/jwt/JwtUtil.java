package org.example.springsecurity.configurations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${spring.security.jwtSecret}")
    private String secretKey;
    @Value("${spring.security.expiration-time}")
    private int expirationTime;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String username, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roles);
        return createToken(claims, username, expirationTime);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Phương thức tạo Token
     *
     * @param claims              thông tin cần thêm
     * @param subject             token này là của ai
     * @param tokenExpirationTime thời hạn token này có thể tồn tại (millisecond)
     * @return trả về một chuỗi token
     */
    private String createToken(Map<String, Object> claims, String subject, long tokenExpirationTime) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(generalSigningKey())
                .compact();
    }

    /**
     * Phương thức trích xuất thông tin từ JWT
     *
     * @param token JWT token cần trích xuất claims
     * @return thông tin bên trong JWT
     */
    private Claims extractAllClaims(String token) {
        JwtParserBuilder parserBuilder = Jwts.parser().verifyWith(generalSigningKey());
        return parserBuilder.build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Phương thức kiểm tra tính hợp lệ của JWT token
     *
     * @param token JWT token cần kiểm tra
     * @param userDetails Thông tin chi tiết người dùng để so sánh
     * @return boolean giá trị true nếu token hợp lệ, false nếu không hợp lệ
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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

    private SecretKey generalSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
