package com.codewithmosh.store.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Integer getUserId() {
        return Integer.valueOf(claims.getSubject());
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
