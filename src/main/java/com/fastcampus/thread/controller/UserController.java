package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.user.User;
import com.fastcampus.thread.model.user.UserAuthenticationResponse;
import com.fastcampus.thread.model.user.UserLoginRequestBody;
import com.fastcampus.thread.model.user.UserSignUpRequestBody;
import com.fastcampus.thread.service.PostService;
import com.fastcampus.thread.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    PostService postService;

    @PostMapping
    public ResponseEntity<User> signUp(

           @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody){

        var user  = userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());

        // 사용자정보를 클라이언트에게 보낼때는 DTo을 통해서필요한 정보만 보내는것이 좋다.

        return  ResponseEntity.ok(user); // ok 함수는 new가 필요없음
        // return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authentication(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody){

        UserAuthenticationResponse accessToken  = userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());

        // 사용자정보를 클라이언트에게 보낼때는 DTo을 통해서필요한 정보만 보내는것이 좋다.

        return  ResponseEntity.ok(accessToken); // ok 함수는 new가 필요없음
        // return new ResponseEntity<>(user, HttpStatus.OK);
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
    public ResponseEntity<User> getUser(@PathVariable String username){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }


    // 배터리
    @PatchMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        // 쿼리 검색어가 있을 경우  해당 유저만
        var user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }


    // GET /users/{username}/posts
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@RequestParam(required = false) String username){

        // 쿼리 검색어가 있을 경우  해당 유저만
        var posts = postService.getPostsByUsername(username);
        return ResponseEntity.ok(users);
        // 아닐 경우 전체

    }
}
