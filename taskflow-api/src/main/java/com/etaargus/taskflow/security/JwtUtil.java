package com.etaargus.taskflow.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key used to SIGN the token (must be long)
    private final String SECRET = "my-super-secret-key-that-is-at-least-256-bits-long";

    // Convert secret string into a Key object
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // CREATE TOKEN
    public String generateToken(Long userId) {

        return Jwts.builder()

                // Store userId inside token (VERY IMPORTANT)
                .setSubject(String.valueOf(userId))

                // when token was created
                .setIssuedAt(new Date())

                // expiration (here: 24 hours)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))

                // sign token with secret key (security)
                .signWith(key, SignatureAlgorithm.HS256)

                // build final string token
                .compact();
    }

    // EXTRACT USER ID FROM TOKEN
    public Long extractUserId(String token) {

        // decode token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // use same secret
                .build()
                .parseClaimsJws(token)
                .getBody();

        // get userId (we stored it in "subject")
        return Long.parseLong(claims.getSubject());
    }

    // VALIDATE TOKEN
    public boolean isValid(String token) {
        try {
            // if parsing works → token is valid
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {
            // invalid / expired token
            return false;
        }
    }
}