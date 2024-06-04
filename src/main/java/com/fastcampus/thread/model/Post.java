package com.fastcampus.thread.model;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Long postId; // Integer가 아닌 Long으로 수정

    private String body;

    private ZonedDateTime createdDateTime; // 글로벌 서비스라 가정하여 ZonedDateTime으로 수정

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(createdDateTime, post.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime);
    }


}
