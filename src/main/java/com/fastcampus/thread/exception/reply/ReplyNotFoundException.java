package com.fastcampus.thread.exception.reply;


import com.fastcampus.thread.exception.ClinetErrorResponse;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ClinetErrorResponse {
    public ReplyNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }

    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Post Not Found");
    }

    public ReplyNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ReplyNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Post Not Found: " + id);
    }
}
