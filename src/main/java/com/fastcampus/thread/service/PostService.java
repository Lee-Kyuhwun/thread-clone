package com.fastcampus.thread.service;


import com.fastcampus.thread.controller.PostPatchRequestBody;
import com.fastcampus.thread.model.Post;
import com.fastcampus.thread.model.PostPostRequestBody;
import com.fastcampus.thread.model.entity.PostEntity;
import com.fastcampus.thread.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if(postOptional.isPresent()){
            Post postToUpate = postOptional.get();
            postToUpate.setBody(postPatchRequestBody.body());
            return postToUpate;
        }else{
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }

    }

    public void deletePost(Long postId) {
        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if(postOptional.isPresent()){
            posts.remove(postOptional.get());
        }else{
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found");
        }

    }

}
