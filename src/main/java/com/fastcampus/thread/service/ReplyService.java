package com.fastcampus.thread.service;


import com.fastcampus.thread.exception.post.PostNotFoundException;
import com.fastcampus.thread.exception.reply.ReplyNotFoundException;
import com.fastcampus.thread.exception.user.UserNotAllowedException;
import com.fastcampus.thread.model.post.Post;
import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.post.PostPatchRequestBody;
import com.fastcampus.thread.model.post.PostPostRequestBody;
import com.fastcampus.thread.model.reply.Reply;
import com.fastcampus.thread.model.reply.ReplyEntity;
import com.fastcampus.thread.model.reply.ReplyPatchRequestBody;
import com.fastcampus.thread.model.reply.ReplyPostRequestBody;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.repository.PostEntityRepository;
import com.fastcampus.thread.repository.ReplyEntityRepository;
import com.fastcampus.thread.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class ReplyService {
    @Autowired
    private ReplyEntityRepository replyEntityRepository;

    @Autowired
    PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntities = postEntityRepository.findById(postId).orElseThrow(() ->new PostNotFoundException(postId));
        List<ReplyEntity> byPost = replyEntityRepository.findByPost(postEntities);
        return byPost.stream().map(Reply::from).toList(); // toList()는 stream을 List로 변환
    }



    public Reply createReply(Long postId, ReplyPostRequestBody replyPostPostRequestBody, UserEntity currentUser) {
        log.debug("current User : {}", currentUser.toString());
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );

        ReplyEntity replyEntity = replyEntityRepository.save(
                ReplyEntity.of(replyPostPostRequestBody.body(), currentUser, postEntity)
        );
        return Reply.from(replyEntity);
    }


    public Reply updateReply(ReplyPatchRequestBody replyPatchRequestBody, Long postId, Long replyId, UserEntity currentUser) {

        postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );
        ReplyEntity replyEntity = replyEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().getUserId().equals(currentUser.getUserId())){
            throw new UserNotAllowedException();
        }
        replyEntity.setBody(replyPatchRequestBody.body());
        var save = replyEntityRepository.save(replyEntity);
        return Reply.from(save);

    }

    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );
        ReplyEntity replyEntity = replyEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));
        if(!replyEntity.getUser().getUserId().equals(currentUser.getUserId())){
            throw new UserNotAllowedException();
        }
        replyEntityRepository.delete(replyEntity);

    }
}
