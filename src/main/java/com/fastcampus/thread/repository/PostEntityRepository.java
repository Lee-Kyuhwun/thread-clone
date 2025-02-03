package com.fastcampus.thread.repository;

import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {


    List<PostEntity> findByUser(UserEntity user); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.
    // select * from post where user_username = ?
}
