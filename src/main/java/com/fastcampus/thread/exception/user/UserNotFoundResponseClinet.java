package com.fastcampus.thread.exception.user;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class UserNotFoundResponseClinet extends ClinetErrorResponse {
    public UserNotFoundResponseClinet(HttpStatus status, String message) {
        super(status, message);
    }

    public UserNotFoundResponseClinet() {
        super(HttpStatus.NOT_FOUND, "User Not Found");
    }


    public UserNotFoundResponseClinet(Long id) {
        super(HttpStatus.NOT_FOUND, "User Not Found: " + id);
    }

    public UserNotFoundResponseClinet(String username) {
        super(HttpStatus.NOT_FOUND, "User Not Found: " + username);
    }
}
