package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {


    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "첫번째 글", ZonedDateTime.now()));
        posts.add(new Post(2L, "2번째 글", ZonedDateTime.now()));
        posts.add(new Post(3L, "3번째 글", ZonedDateTime.now()));
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


    @GetMapping("/api/v1/posts/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable("postId") Long postId){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "첫번째 글", ZonedDateTime.now()));
        posts.add(new Post(2L, "2번째 글", ZonedDateTime.now()));
        posts.add(new Post(3L, "3번째 글", ZonedDateTime.now()));

        Optional<Post> matchingPost = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst(); // filter를 사용하여 postId와 일치하는 post를 찾는다.

        return matchingPost.map(post -> ResponseEntity.ok().body(post)).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
