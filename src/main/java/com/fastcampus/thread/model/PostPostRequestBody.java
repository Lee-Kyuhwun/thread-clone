package com.fastcampus.thread.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public record PostPostRequestBody(
        String body
) {
    public PostPostRequestBody {
    }
}
