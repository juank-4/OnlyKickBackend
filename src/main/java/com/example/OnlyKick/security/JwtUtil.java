package com.example.OnlyKick.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // CLAVE SECRETA: En un proyecto real esto iría en variables de entorno, 
    // pero para este paso la dejaremos aquí para que te funcione a la primera.
    // Debe ser larga (min 32 caracteres) para que sea segura.
    private static final String SECRET_KEY = "MI_CLAVE_SUPER_SECRETA_PARA_EL_BACKEND_DE_ONLYKICK_2024"; 

    // Generar un Token para el usuario
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Dura 10 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer el usuario (email) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validar si el token le pertenece al usuario y no ha expirado
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Métodos auxiliares privados
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        // Convierte la clave secreta en bytes para encriptar
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}