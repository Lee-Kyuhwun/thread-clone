package com.fastcampus.thread.exception.post;


import com.fastcampus.thread.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ClientErrorException {
    public PostNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }

    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Post Not Found");
    }

    public PostNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public PostNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Post Not Found: " + id);
    }
}
