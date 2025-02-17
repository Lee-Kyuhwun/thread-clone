package com.fastcampus.thread.exception;

import org.springframework.http.HttpStatus;

public class InvalidFollowException extends ClinetErrorResponse{


    public InvalidFollowException(HttpStatus status, String message) {
        super(status, message);
    }
    public InvalidFollowException() {
        super(HttpStatus.BAD_REQUEST, "Post Not Found");

    }

    public InvalidFollowException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public InvalidFollowException(Long id) {
        super(HttpStatus.NOT_FOUND, "Post Not Found: " + id);
    }

}
