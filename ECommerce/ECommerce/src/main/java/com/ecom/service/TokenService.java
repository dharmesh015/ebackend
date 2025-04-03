package com.ecom.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service

public class TokenService {

    // You should store this securely in application properties
    @Value("${app.jwt.reset.secret:skdnwjfkswmdnwdiejfbfuewddvqyvwyvwdjwdbjenweuweb}")
    private String jwtSecret;
    
    // Expiration time in milliseconds (24 hours)
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    
    // Generate a secure key for HS512 algorithm
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("skdnwjfkswmdnwdiejfbfuewddvqyvwyvwdjwdbjenweuweb");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    
    /**
     * Generate a reset token for the given email
     * 
     * @param email User's email address
     * @return JWT token
     */
    public String generatePasswordResetToken(String email) {
    	System.err.println("generatePasswordResetToken"+email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "password_reset");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate the reset token and extract the email
     * 
     * @param token JWT token
     * @return email if valid, null if invalid
     */
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Verify this is a password reset token
            if (!"password_reset".equals(claims.get("purpose"))) {
                return null;
            }
            
            return claims.getSubject();
        } catch (Exception e) {
            // Token is invalid or expired
            return null;
        }
    }
    
    public  String getemail(String token) {
    	Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    	  return claims.getSubject();
    }
}