package com.hytz.habitos_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key obtenerFirmaSegura() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()

                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(obtenerFirmaSegura())
                .compact();
    }

    public String extraerUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(obtenerFirmaSegura())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date extraerExpiracion(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(obtenerFirmaSegura())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getExpiration();
    }

    private boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    public boolean esTokenValido(String token, UserDetails userDetails) {
        String userNameDelToken = extraerUsername(token);
        return (userNameDelToken.equals(userDetails.getUsername()) && !esTokenExpirado(token));
    }
}
