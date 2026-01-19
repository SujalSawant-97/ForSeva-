package com.example.Auth_Service.Util;

import com.example.Auth_Service.Model.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private String secret = "9f3c7a1e8d2b4c6a0e7f5a9b1d8c2e4f6a7b9c0d3e5f1a2b4c6d8e9f0a1bhagshvjhdksjd";

    public String generateToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", authUser.getId()); // [cite: 33]
        claims.put("role", authUser.getRole()); // [cite: 34]

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
