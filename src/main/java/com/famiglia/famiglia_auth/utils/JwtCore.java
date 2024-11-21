package com.famiglia.famiglia_auth.utils;

import com.famiglia.famiglia_auth.model.imlp.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtCore {
    @Value("${app.jwtSecret}")
    private String secret;
    @Value("${app.jwtLifetime}")
    private String lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getRole().name());

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(Date.from(Instant.now()))
                .setClaims(claims)
                .setExpiration(Date.from(Instant.now().plusMillis(Long.parseLong(lifetime))))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getNameFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}
