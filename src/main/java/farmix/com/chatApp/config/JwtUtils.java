package farmix.com.chatApp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;

@Component
public class JwtUtils {

    private static final String SECRET = "secretkeyforjwtfjw489tvn8B&Y3nc7rcfreioHDUhDUHDHJDUAInjndeuNDJNJkjnjzif3fucfnwbf7x4nv";
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
    public Authentication validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            String username = claims.getBody().getSubject();
            // Optionally, fetch user roles/authorities from UserRepository or include in JWT claims
            // For simplicity, assign a default "USER" role
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
            return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getMessage());
        }
    }

}
