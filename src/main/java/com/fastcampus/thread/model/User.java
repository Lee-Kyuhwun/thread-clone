package com.fastcampus.thread.model;

import com.fastcampus.thread.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(


        Long userId,
        String username,
        String profile,
        String description,

        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime

) {

    public static User from(UserEntity entity) {

        return new User(
                entity.getUserId(),
                entity.getUsername(),
                entity.getProfile(),
                entity.getDescription(),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
        );
    }
}
