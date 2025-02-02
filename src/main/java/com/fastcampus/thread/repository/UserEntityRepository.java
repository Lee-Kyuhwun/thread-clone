package com.fastcampus.thread.repository;


import com.fastcampus.thread.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByUsername(String username);


    // 전달받은 유저네임이 부분적으로 포함되어 있는 유저엔티티들을 반환한다. (JPA가 알아서 해줌)
    List<UserEntity> findByUsernameContaining(String username);



}
