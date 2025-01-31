package com.fastcampus.thread.service;


import com.fastcampus.thread.exception.user.UserAlreadyExistResponseClinet;
import com.fastcampus.thread.exception.user.UserNotFoundException;
import com.fastcampus.thread.model.user.User;
import com.fastcampus.thread.model.user.UserAuthenticationResponse;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }


    // 회원가입
    public User signUp(String username, String password) {
        // 가입되어있는지 먼저 판단
        userEntityRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistResponseClinet(username);
                });

        var user = UserEntity.of(username, passwordEncoder.encode(password));

        // 단방향 알고리즘으로 비밀번호 암호화

        var savedUserEntity = userEntityRepository.save(user);

        return User.from(savedUserEntity);
    }


    // 로그인
    public UserAuthenticationResponse authenticate(String username, String password) {

        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // 비밀번호가 일치하는지 확인
        if(passwordEncoder.matches(password, userEntity.getPassword())){ // 일치하면
            // 토큰을 생성해서 반환
            String accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        }else{
          throw  new UserNotFoundException();
        }

    }
}
