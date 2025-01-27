package com.fastcampus.thread.exception;

import org.springframework.http.HttpStatus;

public class ClinetErrorResponse extends  RuntimeException{
    private final HttpStatus status;


    public ClinetErrorResponse(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
