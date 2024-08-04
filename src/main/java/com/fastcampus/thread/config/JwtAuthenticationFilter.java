package com.fastcampus.thread.config;

import com.fastcampus.thread.exception.jwt.JwtTokenNotFoundException;
import com.fastcampus.thread.service.JwtService;
import com.fastcampus.thread.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{ // OncePerRequestFilter는 한번만 실행되도록 하는 필터


    @Autowired private final JwtService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //TODO: JWT 토큰을 검증하고 사용자 정보를 SecurityContext에 저장하는 로직을 작성하세요.
        String BREARER_PREFIX = "Bearer ";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();// SecurityContextHolder는 SecurityContext를 제공하는 클래스
        // SecurityContext는 인증된 사용자의 정보를 제공하는 클래스
        if(ObjectUtils.isEmpty(authorization) || !authorization.startsWith(BREARER_PREFIX)){
            throw new JwtTokenNotFoundException("JWT 토큰이 존재하지 않습니다.");
        }


        if(!ObjectUtils.isEmpty(authorization) && authorization.startsWith(BREARER_PREFIX) && securityContext.getAuthentication() == null){
            var accessToken = authorization.substring(BREARER_PREFIX.length());
            var username = jwtService.getUsername(accessToken);
            var userDetails = userService.loadUserByUsername(username);

            // 해당 인증정보를 securityContext에 저장해야함
            // 이를 위해 UsernamePasswordAuthenticationToken을 생성
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// WebAuthenticationDetailsSource는 WebAuthenticationDetails를 생성하는 클래스
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        }

        filterChain.doFilter(request, response);
    }
}
