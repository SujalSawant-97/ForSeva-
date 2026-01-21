package com.example.Auth_Service.Util;

import com.example.Auth_Service.Model.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret ;

    public String generateToken(AuthUser authUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", authUser.getId()); // [cite: 33]
        claims.put("role", authUser.getRole()); // [cite: 34]

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 20)) // 20 hours
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
