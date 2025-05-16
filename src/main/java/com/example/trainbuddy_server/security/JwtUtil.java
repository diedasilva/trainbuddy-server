package com.example.trainbuddy_server.security;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String generateToken(String username, Long userId, List<String> roles) {
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(jti)
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Object id = getAllClaims(token).get("userId");
        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        }
        if (id instanceof Long) {
            return (Long) id;
        }
        throw new IllegalArgumentException("Invalid userId type in JWT");
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractJti(String token) {
        return getAllClaims(token).getId();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return getAllClaims(token).get("roles", List.class);
    }

    public long getExpiration() {
        return expiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public Instant extractIssuedAt(String token) {
        Date iat = getAllClaims(token).getIssuedAt();
        return iat.toInstant();
    }
}
