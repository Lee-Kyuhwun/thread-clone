package com.fastcampus.thread.model.user;

import java.time.ZonedDateTime;

public record User(


        Long userId,
        String username,
        String profile,
        String description,
        Long followerCount,
        Long followingCount,

        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime

) {

    public static User from(UserEntity entity) {

        return new User(
                entity.getUserId(),
                entity.getUsername(),
                entity.getProfile(),
                entity.getDescription(),
                entity.getFollowerCount(),
                entity.getFollowingCount(),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
        );
    }
}
