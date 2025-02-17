package com.fastcampus.thread.service;


import com.fastcampus.thread.exception.FollowAlreadyException;
import com.fastcampus.thread.exception.InvalidFollowException;
import com.fastcampus.thread.exception.user.UserAlreadyExistResponseClinet;
import com.fastcampus.thread.exception.user.UserNotAllowedException;
import com.fastcampus.thread.exception.user.UserNotFoundException;
import com.fastcampus.thread.model.follow.FollowEntity;
import com.fastcampus.thread.model.user.User;
import com.fastcampus.thread.model.user.UserAuthenticationResponse;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.model.user.UserPatchRequestBody;
import com.fastcampus.thread.repository.FollowEntityRepository;
import com.fastcampus.thread.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final FollowEntityRepository followEntityRepository;

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



    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;
        // 검색을 위한 유의미한 데이터가 있는 경우(Blank가 추가된 이유
        if(query != null && !query.isBlank()){
            //TODO: query 검색어 기반, 해당 검색어가 username이 포함되어 있는 유저목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        }else{
            userEntities = userEntityRepository.findAll();

        }
        return userEntities.stream().map(User :: from).toList();
    }


    public User getUser(String username) {
        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.from(userEntity);
    }

    public Object updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser)  {
        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // 현재 유저와 수정하려는 유저가 같은지 확인
        if(!userEntity.equals(currentUser)){
            throw new UserNotAllowedException();
        }


        if(userPatchRequestBody.description() != null){
            userEntity.setDescription(userPatchRequestBody.description());
        }

        // 수정된 유저정보를 저장
        var savedUserEntity = userEntityRepository.save(userEntity);

        return User.from(savedUserEntity);

    }

    @Transactional
    public User follow(String username, UserEntity principal) {
        // 저장되어 있는 유저를 찾음
        var following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // 현재 유저와 수정하려는 유저가 같은지 확인
        if(following.equals(principal)){
            throw new InvalidFollowException("A user cannot unfollow themselves");
        }


        followEntityRepository.findByFollowerAndFollowing(principal, following)
                .ifPresent(followEntity -> {
                    throw new FollowAlreadyException("Already following");
                });
        followEntityRepository.save(FollowEntity.of(principal, following));
        // 카운트 값 수정
        following.setFollowerCount(following.getFollowerCount() + 1);
        principal.setFollowingCount(principal.getFollowingCount() + 1);

        // 저장
        userEntityRepository.save(following);
        userEntityRepository.save(principal);
        // 이렇게 한번에 처리할 수도 있음
        userEntityRepository.saveAll(List.of(following, principal));
        return User.from(following);
    }

    @Transactional
    public void unfollow(String username, UserEntity principal) {
        // 저장되어 있는 유저를 찾음
        var following = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // 현재 유저와 수정하려는 유저가 같은지 확인
        if(following.equals(principal)){
            throw new InvalidFollowException("A user cannot unfollow themselves");
        }
        FollowEntity followEntity = followEntityRepository.findByFollowerAndFollowing(principal, following)
                .orElseThrow(() -> {
                    throw new FollowAlreadyException("Not following");
                });

        followEntityRepository.delete(followEntity);
        // 카운트 값 수정
        following.setFollowerCount(Math.max(0,following.getFollowerCount() - 1));
        principal.setFollowingCount(Math.max(0,principal.getFollowingCount() - 1));

        // 저장
        userEntityRepository.save(following);
        userEntityRepository.save(principal);
        // 이렇게 한번에 처리할 수도 있음'/
        userEntityRepository.saveAll(List.of(following, principal));
         
    }
}
