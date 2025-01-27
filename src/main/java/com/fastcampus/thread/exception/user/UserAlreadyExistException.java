package com.fastcampus.thread.exception.user;


import com.fastcampus.thread.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ClientErrorException {
    public UserAlreadyExistException() {
        super(HttpStatus.CONFLICT, "User Already Exists");
    }

    public UserAlreadyExistException(String username) {
        super(HttpStatus.NOT_FOUND, "User with Username Already Exists" + username + "already exists");
    }

}
