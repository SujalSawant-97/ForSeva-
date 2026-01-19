package com.example.APIGateway.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)   // 🔥 IMPORTANT
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        Jwts.parser()
                .setSigningKey(secret)   // 🔥 IMPORTANT
                .parseClaimsJws(token);
    }
}
