package com.fastcampus.thread.service;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();


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
}
