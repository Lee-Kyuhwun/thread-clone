package com.fastcampus.thread.service;


import com.fastcampus.thread.model.like.LikeEntity;
import com.fastcampus.thread.model.post.PostPatchRequestBody;
import com.fastcampus.thread.exception.post.PostNotFoundException;
import com.fastcampus.thread.exception.user.UserNotAllowedException;
import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostPostRequestBody;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.repository.LikeEntityRepository;
import com.fastcampus.thread.repository.PostEntityRepository;
import com.fastcampus.thread.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    @Autowired
    PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;

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


    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
        log.debug("current User : {}", currentUser.toString());
        PostEntity savedPostEntity = postEntityRepository.save(
                PostEntity.of(postPostRequestBody.body(), currentUser)
        );
        return Post.from(savedPostEntity);
    }

    public Post updatePost(PostPatchRequestBody postPatchRequestBody, Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );

        // user가 같은지 검증하는 로직
        if(!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        var save = postEntityRepository.save(postEntity);
        return Post.from(save);
    }

    public void deletePost(Long postId, UserEntity currentUser) {


        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );
        // user가 같은지 검증하는 로직
        if(!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }
        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username) {

        // 저장되어 있는 유저를 찾음
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<PostEntity> postList = postEntityRepository.findByUser(userEntity);


        return postList.stream().map(Post::from).toList(); // toList()는 stream을 List로 변환
        // return postList.stream().map(Post::from).collect(Collectors.toList()); // toList()는 stream을 List로 변환
    }


    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        Optional<LikeEntity> likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);
        if(likeEntity.isPresent()){
            // 좋아요 취소
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1)); // 음수가 되지 않도록 방어로직
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
        }

        return Post.from(postEntityRepository.save(postEntity));
    }
}
