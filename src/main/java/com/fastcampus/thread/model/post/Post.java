package com.fastcampus.thread.model.post;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 json으로 변환하지 않음
public record Post(
        Long id,
        String body,
        ZonedDateTime createdDateTime,

        ZonedDateTime updatedDateTime,

        ZonedDateTime deletedDateTime
) {

    public static Post from(PostEntity postEntity){
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime()
        );
    }
}