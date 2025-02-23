package com.fastcampus.thread.model.user;

import java.time.ZonedDateTime;

public record Follower(


        Long userId,
        String username,
        String profile,
        String description,
        Long followerCount,
        Long followingCount,

        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isFollowing,
        ZonedDateTime followedDateTime

) {


    public static Follower from(User user, ZonedDateTime followedDateTime) {

        return new Follower(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followerCount(),
                user.followingCount(),
                user.createdDateTime(),
                user.updatedDateTime(),
                user.isFollowing(),
                followedDateTime
        );
    }
}
