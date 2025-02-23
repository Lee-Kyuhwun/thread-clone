package com.fastcampus.thread.repository;

import com.fastcampus.thread.model.follow.FollowEntity;
import com.fastcampus.thread.model.like.LikeEntity;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {


    List<FollowEntity> findByFollower(UserEntity user); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.
    // select * from post where user_username = ?

    List<FollowEntity> findByFollowing(UserEntity user); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.


    //없을수도 있기때문에 Optional로 감싸준다.
    // Opotoinal은 값이 있을수도 있고 없을수도 있을때 사용한다.
    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity user, UserEntity following); // findby + 필드명 + 조건 이렇게하면 해당 필드로 검색이 가능하다.
}
