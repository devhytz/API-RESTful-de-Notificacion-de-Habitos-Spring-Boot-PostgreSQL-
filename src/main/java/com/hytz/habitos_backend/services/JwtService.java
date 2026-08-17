package com.hytz.habitos_backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Spring inyecta automáticamente el valor que pusiste en el application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * PASO A: Generar el token con los datos del usuario.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                // 1. A quién le pertenece este token (El "Username" o Email)
                .subject(userDetails.getUsername())
                // 2. Fecha de emisión (Hoy, ahora mismo)
                .issuedAt(new Date(System.currentTimeMillis()))
                // 3. Fecha de expiración (Le daremos 24 horas de validez)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                // 4. Firmar matemáticamente el token usando nuestra llave secreta
                .signWith(getSignInKey())
                // 5. Construir y empaquetar el token en un String
                .compact();
    }

    /**
     * PASO B: Convertir el texto de properties en una llave criptográfica real.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Usamos la misma llave para abrir el candado
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * PASO D: Extraer específicamente el correo (Username) del token.
     */
    public String extractUsername(String token) {
        // Obtenemos todos los claims y extraemos el Subject (que configuramos como el email)
        return extractAllClaims(token).getSubject();
    }

    /**
     * PASO E: Validar si el token le pertenece al usuario y no ha expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Es válido si el email del token coincide con el de la base de datos Y si no ha expirado
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * PASO F: Comprobar la fecha de expiración.
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}