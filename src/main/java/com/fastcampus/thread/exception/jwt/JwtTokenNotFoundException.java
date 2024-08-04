package com.fastcampus.thread.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokenNotFoundException extends JwtException {


    public JwtTokenNotFoundException(String message) {
        super("JWT Token Not Found Exception: " + message);
    }
}
