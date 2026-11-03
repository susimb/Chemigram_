
package com.chemigram.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey claveSecreta;
    private final long tiempoExpiracion;

    public JwtService(
            @Value("${app.jwt.secret}") String secreto,
            @Value("${app.jwt.expiration-ms}") long tiempoExpiracion) {

        byte[] claveBytes = Decoders.BASE64.decode(secreto);
        this.claveSecreta = Keys.hmacShaKeyFor(claveBytes);
        this.tiempoExpiracion = tiempoExpiracion;
    }

    public String generarToken(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(
                ahora.getTime() + tiempoExpiracion
        );

        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(claveSecreta)
                .compact();
    }

    public String obtenerEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            obtenerEmail(token);
            return true;
        } catch (io.jsonwebtoken.JwtException
                 | IllegalArgumentException excepcion) {
            return false;
        }
    }
}
