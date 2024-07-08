package com.fastcampus.thread.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PostPostRequestBody {

    private String body;

    public PostPostRequestBody(String body) {
        this.body = body;
    }

    public PostPostRequestBody() {
    }
}
