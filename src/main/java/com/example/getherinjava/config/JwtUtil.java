package com.example.getherinjava.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "this_is_a_very_long_and_secure_jwt_secret_key_12345";

    private static final long EXP_TIME = 12 * 60 * 60 * 1000;

    public String generateToken(String userName, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("username", userName)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXP_TIME)
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes(StandardCharsets.UTF_8)
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(
                        SECRET.getBytes(StandardCharsets.UTF_8)
                )
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(
                        SECRET.getBytes(StandardCharsets.UTF_8)
                )
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(
                            SECRET.getBytes(StandardCharsets.UTF_8)
                    )
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}