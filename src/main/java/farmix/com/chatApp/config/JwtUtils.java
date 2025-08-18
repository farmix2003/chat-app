package farmix.com.chatApp.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {

    private static final String SECRET = "secretkeyforjwt";
    private static final long EXPIRATION_TIME = 864000000;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
                .setExpiration(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().plusSeconds(EXPIRATION_TIME)))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
