package com.fastcampus.thread.service;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();


    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String token) {
        return getSubject(token);
    }

    private String generateToken(String username) {

        var now = new Date();

        var exp = new Date(now.getTime() + 1000 * 60 * 60 * 3); // 3시간
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(exp)
                .setSubject(username)
                .signWith(key)
                .compact();

    }

    private String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getSubject();
        }catch (JwtException e){
            log.error(e.getMessage());
            throw  e;
        }

    }

}
