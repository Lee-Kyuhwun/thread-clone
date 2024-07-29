package com.fastcampus.thread.repository;

import com.fastcampus.thread.model.Post;
import com.fastcampus.thread.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {


}
