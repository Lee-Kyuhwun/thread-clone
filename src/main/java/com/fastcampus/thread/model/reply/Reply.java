package com.fastcampus.thread.model.reply;

import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 json으로 변환하지 않음
public record Reply(
        Long id,
        String body,

        User user,
        Post post,
        ZonedDateTime createdDateTime,

        ZonedDateTime updatedDateTime,

        ZonedDateTime deletedDateTime
) {

    public static Reply from(ReplyEntity replyEntity){
        return new Reply(
                replyEntity.getReplyId(),
                replyEntity.getBody(),
                User.from(replyEntity.getUser()),
                Post.from(replyEntity.getPost()),
                replyEntity.getCreatedDateTime(),
                replyEntity.getUpdatedDateTime(),
                replyEntity.getDeletedDateTime()
        );
    }
}