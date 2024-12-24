package com.example.attendance.Util;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "XnGTbUc9sZB851mBpsh5tUsd6e/6K35ZjO/oVMpBguI=";

    private static final long TOKEN_VALIDITY = 3600000L;

      // Generate a token using user details
      public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

     // Extract email from token
     public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Check if the token is valid (not expired and correctly signed)
    public boolean isTokenValid(String token, String userEmail) {
        return (extractEmail(token).equals(userEmail) && !isTokenExpired(token));
    }

    // Check if the token has expired
    @SuppressWarnings("deprecation")
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date(0, 0, 0));
    }

    // Get claims from token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}