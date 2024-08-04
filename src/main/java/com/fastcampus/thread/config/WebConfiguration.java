package com.fastcampus.thread.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired private JwtExceptionFilter jwtExceptionFilter;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://127.0.0.1:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfiguration); // url 베이스로

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf((csrf) -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());// Customizer.withDefaults() is a shortcut for httpBasic().disable()

        return http.build();
    }
}
