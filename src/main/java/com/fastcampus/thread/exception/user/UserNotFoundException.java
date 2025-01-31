package com.fastcampus.thread.exception.user;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClinetErrorResponse {
    public UserNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User Not Found");
    }


    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "User Not Found: " + id);
    }

    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, "User Not Found: " + username);
    }
}
