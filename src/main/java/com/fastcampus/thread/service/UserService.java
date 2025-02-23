package com.fastcampus.thread.service;


import com.fastcampus.thread.exception.FollowAlreadyException;
import com.fastcampus.thread.exception.InvalidFollowException;
import com.fastcampus.thread.exception.post.PostNotFoundException;
import com.fastcampus.thread.exception.user.UserAlreadyExistResponseClinet;
import com.fastcampus.thread.exception.user.UserNotAllowedException;
import com.fastcampus.thread.exception.user.UserNotFoundException;
import com.fastcampus.thread.model.follow.FollowEntity;
import com.fastcampus.thread.model.like.LikeEntity;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.reply.Reply;
import com.fastcampus.thread.model.user.*;
import com.fastcampus.thread.repository.FollowEntityRepository;
import com.fastcampus.thread.repository.LikeEntityRepository;
import com.fastcampus.thread.repository.PostEntityRepository;
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

    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
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



    public List<User> getUsers(String query,UserEntity currentUser) {
        List<UserEntity> userEntities;
        // 검색을 위한 유의미한 데이터가 있는 경우(Blank가 추가된 이유
        if(query != null && !query.isBlank()){
            //TODO: query 검색어 기반, 해당 검색어가 username이 포함되어 있는 유저목록 가져오기
            userEntities = userEntityRepository.findByUsernameContaining(query);
        }else{
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(user -> getUserWithFollowingStatus(user,currentUser)).toList();
    }


    public User getUser(String username,UserEntity currentUser) {
        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return getUserWithFollowingStatus(userEntity,currentUser);
    }

    private User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser){
        boolean isFollowing = followEntityRepository.findByFollowerAndFollowing(currentUser, userEntity)
                .isPresent();
        return User.from(userEntity,isFollowing);
    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser)  {
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
        return User.from(following,true);
    }

    @Transactional
    public User unfollow(String username, UserEntity principal) {
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
         return User.from(following,false);
    }

    public List<User> getFollowersByUsername(String username,UserEntity currentUser) {
        // 저장되어 있는 유저를 찾음
        UserEntity followings = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // 팔로잉 엔티티를 가져오면 모두 동일한 팔로잉을 가지게 될것이고
        // 이를 팔로워로 변환해서 반환
        // username을 팔로우 하는 사람들을 리턴하는 것이다.
        List<FollowEntity> followEntities = followEntityRepository.findByFollowing(followings);

        return followEntities.stream()
                .map(FollowEntity::getFollower)
                .map(user -> getUserWithFollowingStatus(user,currentUser))
                .toList();
    }

    public List<User> getFollowingByUsername(String username,UserEntity currentUser) {

        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return followEntityRepository.findByFollower(userEntity).stream()
                .map(FollowEntity::getFollowing)
                .map(user -> getUserWithFollowingStatus(user,currentUser))
                .toList();
    }


    public List<Follower> getFolllowersByUserName(String username, UserEntity authentication) {
        // 저장되어 있는 유저를 찾음
        UserEntity followings = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        var followEntities = followEntityRepository.findByFollowing(followings);
        return followEntities.stream()
                .map(
                        followEntity -> Follower.from(getUserWithFollowingStatus(followEntity.getFollower(),authentication),
                                followEntity.getCreatedDateTime())
                ).toList();

    }

    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {
        // 게시물 기준으로 좋아요 누른 사람들을 가져옴
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));


        List<LikeEntity> likeEntities = likeEntityRepository.findByPost(postEntity);

        return likeEntities.stream()
                .map(
                        likeEntity -> getLikedUserWithFollowingStatus(likeEntity, currentUser, postEntity, postId)
                ).toList();
    }


    private LikedUser getLikedUserWithFollowingStatus(LikeEntity likeEntity, UserEntity currentUser, PostEntity postEntity, Long postId){
        List<LikeEntity> likeEntities = likeEntityRepository.findByPost(postEntity);
        var user = likeEntity.getUser();
        User userWithFollowingStatus = getUserWithFollowingStatus(user, currentUser);
        return LikedUser.from(userWithFollowingStatus, likeEntity.getCreatedDateTime(),postId);
    }

    public List<LikedUser> getLikedUsersByUser(String username, UserEntity authentication) {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<PostEntity> postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities.stream().flatMap(
                // flatMap 메서드는 스트림의 각 요소에 함수를 적용하고 생성된 각각의 스트림을 하나의 스트림으로 연결하는 기능을 수행합니다.
                // 여기서는 각 postEntity에 대해 좋아요를 찾고 이를 LikedUser 객체의 스트림으로 변환합니다.
                postEntity -> likeEntityRepository.findByPost(postEntity).stream()
                        .map(likeEntity -> getLikedUserWithFollowingStatus(likeEntity, authentication, postEntity, postEntity.getPostId()))
        ).toList();

    }
}
