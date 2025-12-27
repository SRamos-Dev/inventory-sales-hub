package com.github.inventorysaleshub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}") 
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    // --- NUEVO: Método Helper para generar la clave criptográfica ---
    private SecretKey getSignInKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. GENERAR TOKEN (Actualizado)
    public String generateToken(Authentication authentication) {
        // Asumiendo que usas un UserDetails o similar principal
        String username = authentication.getName(); 
        
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration * 1000L))
                .signWith(getSignInKey()) // Firma con la nueva Key, no con String
                .compact();
    }

    // 1b. GENERAR TOKEN CON USERNAME Y ROLE
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiration * 1000L))
                .signWith(getSignInKey())
                .compact();
    }

    // 2. OBTENER USUARIO DEL TOKEN (Actualizado - El error de parserBuilder)
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Nueva sintaxis
                .build()
                .parseSignedClaims(token)
                .getPayload() // Antes getBody()
                .getSubject();
    }

    // 2b. OBTENER EMAIL DEL TOKEN (alias)
    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }

    // 2c. OBTENER ROLE DEL TOKEN
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

    // 3. VALIDAR TOKEN (Actualizado)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Token mal formado");
        } catch (UnsupportedJwtException e) {
            System.err.println("Token no soportado");
        } catch (ExpiredJwtException e) {
            System.err.println("Token expirado");
        } catch (IllegalArgumentException e) {
            System.err.println("Token vacío");
        } catch (SignatureException e) {
            System.err.println("Firma fallida");
        }
        return false;
    }
}

