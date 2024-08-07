package com.fastcampus.thread.repository;


import com.fastcampus.thread.model.User;
import com.fastcampus.thread.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByUsername(String username);

}
