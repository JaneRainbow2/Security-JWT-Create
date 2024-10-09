package com.softserve.itacademy.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String secretKey;

    private final Key signingKey;

    public JwtService() {
        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String extractUserName(String token) {
        log.info("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.info("Extracting claims from token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Generating token with extra claims for user: {}", userDetails.getUsername());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(signingKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        log.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        log.info("Extracting expiration date from token");
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}