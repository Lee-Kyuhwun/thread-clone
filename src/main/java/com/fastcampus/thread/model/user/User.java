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
        ZonedDateTime updatedDateTime,
        Boolean isFollowing

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
                entity.getUpdatedDateTime(),
                null
        );
    }

    public static User from(UserEntity entity, Boolean isFollowing) {

        return new User(
                entity.getUserId(),
                entity.getUsername(),
                entity.getProfile(),
                entity.getDescription(),
                entity.getFollowerCount(),
                entity.getFollowingCount(),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime(),
                isFollowing
        );
    }
}
