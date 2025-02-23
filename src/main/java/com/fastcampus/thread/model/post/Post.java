package com.fastcampus.thread.model.post;

import com.fastcampus.thread.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 json으로 변환하지 않음
public record Post(
        Long id,
        String body,

        Long repliesCount,

        Long likesCount,

        ZonedDateTime createdDateTime,

        ZonedDateTime updatedDateTime,

        ZonedDateTime deletedDateTime,
        User user, // 클라이언트한테 내려줄때는 UserDto로 내려준다.

        Boolean isLiked // 클라이언트한테 내려줄때는 UserDto로 내려준다.
) {

    public static Post from(PostEntity postEntity){
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getRepliesCount(),
                postEntity.getLikesCount(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime(),
                User.from(postEntity.getUser()),
                null
        );
    }

    // post record 생성ㅇ할때 우리가 원하는 상태값 나오도록
        public static Post from(PostEntity postEntity,boolean isLiking){
            return new Post(
                    postEntity.getPostId(),
                    postEntity.getBody(),
                    postEntity.getRepliesCount(),
                    postEntity.getLikesCount(),
                    postEntity.getCreatedDateTime(),
                    postEntity.getUpdatedDateTime(),
                    postEntity.getDeletedDateTime(),
                    User.from(postEntity.getUser()),
                    isLiking
            );
        }

}