package com.example.Custom_Authentication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret_key;

    public String generateJwtToken(String email) {

        return Jwts.builder().
                setSubject(email).
                setIssuedAt(new Date()).
                setExpiration(new Date(System.currentTimeMillis()+86400000)).
                signWith(SignatureAlgorithm.HS256,secret_key).
                compact();
    }


    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {

        Jwts.parserBuilder()
                .setSigningKey(secret_key)
                .build()
                .parseClaimsJws(token);

        return true;
    }
}
