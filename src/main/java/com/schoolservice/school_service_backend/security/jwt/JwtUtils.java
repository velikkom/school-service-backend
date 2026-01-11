package com.schoolservice.school_service_backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    /* =============================
       TOKEN GENERATION (0.11.5)
    ============================== */
    public String generateJwtToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())   // ⬅️ setSubject
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + jwtExpirationMs
                ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* =============================
       GET USERNAME
    ============================== */
    public String getUserNameFromJwtToken(String token) {
        return getClaims(token).getSubject();
    }

    /* =============================
       GET ROLES
    ============================== */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token) {
        return (List<String>) getClaims(token).get("roles");
    }

    /* =============================
       TOKEN VALIDATION
    ============================== */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
