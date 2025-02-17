package com.fastcampus.thread.exception;


import org.springframework.http.HttpStatus;

public class FollowAlreadyException extends ClinetErrorResponse {
    public FollowAlreadyException() {
        super(HttpStatus.CONFLICT, "User Already Exists");
    }

    public FollowAlreadyException(String username) {
        super(HttpStatus.NOT_FOUND, "User with Username Already Exists" + username + "already exists");
    }

}
