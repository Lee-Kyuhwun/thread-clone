package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.user.*;
import com.fastcampus.thread.service.PostService;
import com.fastcampus.thread.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    PostService postService;

    @PostMapping
    public ResponseEntity<User> signUp(

           @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody){

        var user  = userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());

        // 사용자정보를 클라이언트에게 보낼때는 DTo을 통해서필요한 정보만 보내는것이 좋다.

        return  ResponseEntity.ok(user); // ok 함수는 new가 필요없음
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authentication(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody){

        UserAuthenticationResponse accessToken  = userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());

        // 사용자정보를 클라이언트에게 보낼때는 DTo을 통해서필요한 정보만 보내는것이 좋다.

        return  ResponseEntity.ok(accessToken); // ok 함수는 new가 필요없음
    }



    @GetMapping()
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query){

        // 쿼리 검색어가 있을 경우  해당 유저만
        var users = userService.getUsers(query);
        return ResponseEntity.ok(users);
        // 아닐 경우 전체

    }

    // 단건 조회
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username,
                                        @RequestBody UserPatchRequestBody userPatchRequestBody,
                                        Authentication authentication){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var user = userService.updateUser(username,userPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }


    // 수정(자기소개 등등)`
    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    // 특정유저가 작성한 모든 게시글 조회
    // GET /users/{username}/posts
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username,
                                                         Authentication authentication){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var posts = postService.getPostsByUsername(username,(UserEntity) authentication);
        return ResponseEntity.ok(posts);
        // 아닐 경우 전체

    }

    // 팔로우 하기
    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username,
                                       Authentication authentication){
        // 쿼리 검색어가 있을 경우  해당 유저만
        User follow = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(follow);

    }

    // 팔로우 목록
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowersByUser(@PathVariable String username){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var followers = userService.getFollowersByUsername(username);
        return ResponseEntity.ok(followers);
        // 아닐 경우 전체

    }

    // 팔로우 목록
    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(@PathVariable String username){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var followings = userService.getFollowingByUsername(username);
        return ResponseEntity.ok(followings);
        // 아닐 경우 전체

    }


    // 언팔로우 하기
    @DeleteMapping("/{username}/follows")
    public ResponseEntity<Void> unfollow(@PathVariable String username,
                                       Authentication authentication){ // 언팔로우대도 url은 동일
        // 쿼리 검색어가 있을 경우  해당 유저만
        userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
        // 아닐 경우 전체

    }
}
