package com.fastcampus.thread.exception;

import org.springframework.http.HttpStatus;

public class ServerErrorResponse extends  RuntimeException{
    private final HttpStatus status;


    public ServerErrorResponse(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
