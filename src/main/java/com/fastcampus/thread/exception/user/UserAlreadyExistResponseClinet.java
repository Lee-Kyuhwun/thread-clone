package com.fastcampus.thread.exception.user;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistResponseClinet extends ClinetErrorResponse {
    public UserAlreadyExistResponseClinet() {
        super(HttpStatus.CONFLICT, "User Already Exists");
    }

    public UserAlreadyExistResponseClinet(String username) {
        super(HttpStatus.NOT_FOUND, "User with Username Already Exists" + username + "already exists");
    }

}
