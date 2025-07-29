package com.example.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    public final String niewzykle_wazny_secret_jwt;
    public JwtService(@Value("${jwt.niewzykle_wazny_secret_jwt}") String niewzykle_wazny_secret_jwt){
        this.niewzykle_wazny_secret_jwt = niewzykle_wazny_secret_jwt;
    }
    public void validateToken(final String token) throws ExpiredJwtException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(niewzykle_wazny_secret_jwt);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generate_JWT_Token(String username, int duration){
        Map<String, Object> claims = new HashMap<>();
        return create_JWT_Token(claims,username,duration);
    }
    public String create_JWT_Token(Map<String,Object> claims, String username,int duration){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+duration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String getSubject(final String token){
        return Jwts
                .parser()
                .setSigningKey(niewzykle_wazny_secret_jwt)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String refreshToken(final String token, int duration){
        String username = getSubject(token);
        return generate_JWT_Token(username, duration);
    }
}
