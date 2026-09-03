package edu.co.sena.worksite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // ⚠️ En un proyecto real esta clave debe venir de una variable de entorno,
    // nunca escrita directamente en el código fuente. Para el proyecto académico
    // basta con dejarla aquí, pero cámbiala por algo propio (mínimo 32 caracteres).
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "worksite-panameño-avraham-chacon-kelly".getBytes()
    );

    private final long EXPIRATION_MS = 1000L * 60 * 60 * 8; // 8 horas

    public String generarToken(int idUsuario, String correo, String rol) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("id", idUsuario)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    public Claims validarYObtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
