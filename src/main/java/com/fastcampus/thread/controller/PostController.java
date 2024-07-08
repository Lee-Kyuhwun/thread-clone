package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.Post;
import com.fastcampus.thread.model.PostPostRequestBody;
import com.fastcampus.thread.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }




    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable("postId") Long postId){
        Optional<Post> matchingPost = postService.getPostByPostId(postId);
        return matchingPost.map(post -> ResponseEntity.ok().body(post)).orElseGet(() -> ResponseEntity.notFound().build());
    }    
    
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){
        Post createdPost = postService.createPost(postPostRequestBody);
        return ResponseEntity.ok(createdPost);
    }
    // ReuqestBody 정상동작하려면 PostPostRequestBody에 기본생성자가 있어야함




}
