package com.fastcampus.thread.service;


import com.fastcampus.thread.controller.PostPatchRequestBody;
import com.fastcampus.thread.model.Post;
import com.fastcampus.thread.model.PostPostRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L, "첫번째 글", ZonedDateTime.now()));
        posts.add(new Post(2L, "2번째 글", ZonedDateTime.now()));
        posts.add(new Post(3L, "3번째 글", ZonedDateTime.now()));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Optional<Post> getPostByPostId(Long postId) {
        return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
    }


    public Post createPost(PostPostRequestBody postPostRequestBody) {
        var newPostId = posts.stream().mapToLong(Post::getPostId).max().orElse(0L) + 1;
        Post post = new Post(newPostId, postPostRequestBody.body(), ZonedDateTime.now());
        // max()는 long을 반환하기 때문에 mapToLong()을 사용해야함
        //
        posts.add(post);
        return post;
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
