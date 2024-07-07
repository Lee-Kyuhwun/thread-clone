package com.fastcampus.thread.service;


import com.fastcampus.thread.model.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L, "첫번째 글", null));
        posts.add(new Post(2L, "2번째 글", null));
        posts.add(new Post(3L, "3번째 글", null));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Optional<Post> getPost(Long postId) {
        return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
    }
}
