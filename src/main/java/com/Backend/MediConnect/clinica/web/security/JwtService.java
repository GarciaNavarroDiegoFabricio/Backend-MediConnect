package com.Backend.MediConnect.clinica.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarAccessToken(Long idUsuario, Integer idRol) {
        return Jwts.builder()
                .subject(String.valueOf(idUsuario))
                .claim("rol", idRol)
                .claim("tipo", "ACCESS")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getKey())
                .compact();
    }

    public String generarRefreshToken(Long idUsuario, Integer idRol) {
        return Jwts.builder()
                .subject(String.valueOf(idUsuario))
                .claim("rol", idRol)
                .claim("tipo", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getKey())
                .compact();
    }

    public Long extraerIdUsuario(String token) {
        return Long.parseLong(extraerClaim(token, Claims::getSubject));
    }

    public Integer extraerRol(String token) {
        return extraerClaim(token, claims -> claims.get("rol", Integer.class));
    }

    public String extraerTipo(String token) {
        return extraerClaim(token, claims -> claims.get("tipo", String.class));
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}