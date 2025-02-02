package com.fastcampus.thread.exception.user;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClinetErrorResponse {
    public UserNotAllowedException(HttpStatus status, String message) {
        super(status, message);
    }

    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN, "User Not Allowed");
    }


    public UserNotAllowedException(Long id) {
        super(HttpStatus.FORBIDDEN, "User Not Allowed: " + id);
    }

    public UserNotAllowedException(String username) {
        super(HttpStatus.FORBIDDEN, "User Not Allowed: " + username);
    }
}
