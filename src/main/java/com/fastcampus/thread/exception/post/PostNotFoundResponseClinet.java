package com.fastcampus.thread.exception.post;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class PostNotFoundResponseClinet extends ClinetErrorResponse {
    public PostNotFoundResponseClinet(HttpStatus status, String message) {
        super(status, message);
    }

    public PostNotFoundResponseClinet() {
        super(HttpStatus.NOT_FOUND, "Post Not Found");
    }

    public PostNotFoundResponseClinet(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public PostNotFoundResponseClinet(Long id) {
        super(HttpStatus.NOT_FOUND, "Post Not Found: " + id);
    }
}
