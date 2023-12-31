package com.coherent.test.task.infrastructure.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static final String ACCESS_TOKEN_SECRET = "BbYp3s6v9y$B&E)H@McQfTjWnZr4u7x!";

    private static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 86400000L;

    private TokenUtils() {
    }

    public static String createToken(String userName) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("userName", userName);

        return Jwts.builder()
                .setSubject(userName)
                .addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            var userName = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());

        } catch (JwtException e) {
            return null;

        }
    }

}
