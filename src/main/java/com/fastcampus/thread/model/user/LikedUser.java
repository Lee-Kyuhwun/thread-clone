package com.fastcampus.thread.model.user;

import java.time.ZonedDateTime;


// 사용자가 좋아요를 누른 사용자의 정보를 담는 클래스
public record LikedUser(


        Long userId,
        String username,
        String profile,
        String description,
        Long followerCount,
        Long followingCount,

        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isFollowing,
        ZonedDateTime likedDateTime,
        Long likedPostId

) {


    public static LikedUser from(User user, ZonedDateTime likedDateTime,Long likedPostId) {

        return new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followerCount(),
                user.followingCount(),
                user.createdDateTime(),
                user.updatedDateTime(),
                user.isFollowing(),
                likedDateTime,
                likedPostId
        );
    }
}
