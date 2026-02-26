package com.matiasanastasio.biblioteca.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration-minutes}") long expirationMinutes
    ){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generarToken(UserDetails user){
        Instant now = Instant.now();

        List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        
        return Jwts.builder()
            .subject(user.getUsername())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(key,Jwts.SIG.HS256)
                .compact();
    }

    public String extraerUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean esValido(String token, UserDetails user) {
        Claims c = parseClaims(token);
        return c.getSubject().equals(user.getUsername()) && c.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
