package com.example.navire.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "secret_key_example";
    private final long EXPIRATION = 86400000; // 1 day

    public String generateToken(String mail) {
        return Jwts.builder()
                .setSubject(mail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractMail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, String mail) {
        String extractedMail = extractMail(token);
        return (extractedMail.equals(mail) && !isTokenExpired(token));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
