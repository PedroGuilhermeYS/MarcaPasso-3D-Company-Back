package br.edu.ifpe.MarcaPasso3D.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(Long id, String email, String role, String nome) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role)
                .claim("nome", nome)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Long extrairId(String token) {
        return Long.valueOf(extrairClaims(token).getSubject());
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).get("email", String.class);
    }

    public String extrairRole(String token) {
        return extrairClaims(token).get("role", String.class);
    }
}