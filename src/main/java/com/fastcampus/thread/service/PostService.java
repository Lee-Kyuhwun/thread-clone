package com.fastcampus.thread.service;


import com.fastcampus.thread.controller.PostPatchRequestBody;
import com.fastcampus.thread.exception.post.PostNotFoundResponseClinet;
import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostPostRequestBody;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostEntityRepository postEntityRepository;


    public List<Post> getPosts() {
        var postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList(); // toList()는 stream을 List로 변환
    }

    public Post getPostByPostId(Long postId) {

        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found") // null인 경우 예외처리
                );
        return Post.from(postEntity);
    }


    public Post createPost(PostPostRequestBody postPostRequestBody) {
        var postEntity = new PostEntity();
        postEntity.setBody(postPostRequestBody.body());
        PostEntity savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(PostPatchRequestBody postPatchRequestBody, Long postId) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundResponseClinet(postId) // null인 경우 예외처리
                );
        postEntity.setBody(postPatchRequestBody.body());
        var save = postEntityRepository.save(postEntity);
        return Post.from(save);
    }

    public void deletePost(Long postId) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundResponseClinet(postId) // null인 경우 예외처리
                );
        postEntityRepository.delete(postEntity);
    }

}
