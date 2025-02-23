package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostPatchRequestBody;
import com.fastcampus.thread.model.post.PostPostRequestBody;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.service.PostService;
import com.fastcampus.thread.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {



    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication){
        log.info("getPosts");
        var posts = postService.getPosts((UserEntity) authentication);
        return ResponseEntity.ok(posts);
    }




    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable("postId") Long postId,
                                                Authentication authentication
    //좋아요 상태 파악하기 위해 Authentication 추가
    ){
        var post = postService.getPostByPostId(postId, (UserEntity) authentication);
        return ResponseEntity.ok(post);
    }
    
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody,
                                           Authentication authentication){
        /**
         * principal에서 userDetail을 넣어줌
         * principal값에다가 토큰을 통해 가져온 userDetails 정보를 넣어줬고
         * 이렇게 넣어준 사용자 정보를 Authentication을 통해서 가져올 수 있다.
         * */
        Object principal = authentication.getPrincipal();
        var createdPost = postService.createPost(postPostRequestBody,(UserEntity) principal);
        return ResponseEntity.ok(createdPost);
    }
    // ReuqestBody 정상동작하려면 PostPostRequestBody에 기본생성자가 있어야함

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody PostPatchRequestBody postPatchRequestBody,
                                           @PathVariable("postId") Long postId,
                                           Authentication authentication){
        Object principal = authentication.getPrincipal();
        var post = postService.updatePost(postPatchRequestBody, postId,(UserEntity) principal);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                           Authentication authentication){
        Object principal = authentication.getPrincipal();
        postService.deletePost(postId,(UserEntity) principal);
        return ResponseEntity.noContent().build(); // 204
    }

    // 좋아요 생성
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable("postId") Long postId,
                                                 Authentication authentication){
        Object principal = authentication.getPrincipal();
        var post = postService.toggleLike(postId,(UserEntity) principal);
        return ResponseEntity.ok(post);
    }

}
