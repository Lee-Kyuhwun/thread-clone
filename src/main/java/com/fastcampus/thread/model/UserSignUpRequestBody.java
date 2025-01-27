package com.fastcampus.thread.model;


public record UserSignUpRequestBody(

        String username,
        String password
        ) {
    public UserSignUpRequestBody {

    }
}
