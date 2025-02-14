package com.fastcampus.thread.repository;

import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.reply.ReplyEntity;
import com.fastcampus.thread.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {


    List<ReplyEntity> findByUser(UserEntity user); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.
    // select * from post where user_username = ?

    List<ReplyEntity> findByUserUsername(String username); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.

    List<ReplyEntity> findByPost(PostEntity post); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.
}
