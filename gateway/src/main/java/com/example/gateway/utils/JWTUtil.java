package com.example.gateway.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JWTUtil {
    public final String niewzykle_wazny_secret_jwt;
    public JWTUtil(@Value("${jwt.niewzykle_wazny_secret_jwt}") String niewzykle_wazny_secret_jwt){
        this.niewzykle_wazny_secret_jwt = niewzykle_wazny_secret_jwt;
    }
    public void validateToken(final String token){
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(niewzykle_wazny_secret_jwt);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
