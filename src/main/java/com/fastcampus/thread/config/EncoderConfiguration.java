package com.fastcampus.thread.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // bcrypt 암호화는 BCryptPasswordEncoder를 사용한다.

        // Bcyrpt는 유출
    }
    public String encode(String password) {
        return password;
    }


}
