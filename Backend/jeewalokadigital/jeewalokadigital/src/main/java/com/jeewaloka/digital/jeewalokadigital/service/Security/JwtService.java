package com.jeewaloka.digital.jeewalokadigital.service.Security;

import com.jeewaloka.digital.jeewalokadigital.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    public Map<String, String> generateTokens(UserDetails userDetails) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", generateToken(new HashMap<>(), userDetails, 1000*30));
        tokens.put("refreshToken",generateToken(new HashMap<>(), userDetails, 1000*60*60*24*7));
        return tokens;
    }

    // Generate token with extra claims
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * jwtProperties.getTokenExpirationAfterDays()))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, int expirationTime) {
        System.out.println("entered jwt service to generate token");
        if(!extraClaims.containsKey("role")){
            System.out.println("inside role");
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            extraClaims.put("role",role);
            System.out.println("role work finished");
        }
        System.out.println("if condition is not there");
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationTime)))
                .signWith(getSigningKey())
                .compact();
    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println("token valid has reached");
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        System.out.println("check expired or not");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}