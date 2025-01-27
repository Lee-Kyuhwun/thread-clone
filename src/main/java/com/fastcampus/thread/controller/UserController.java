package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.User;
import com.fastcampus.thread.model.UserSignUpRequestBody;
import com.fastcampus.thread.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody UserSignUpRequestBody userSignUpRequestBody){

        var user  = userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());

        // 사용자정보를 클라이언트에게 보낼때는 DTo을 통해서필요한 정보만 보내는것이 좋다.

        return  ResponseEntity.ok(user); // ok 함수는 new가 필요없음
        // return new ResponseEntity<>(user, HttpStatus.OK);
    }



}
