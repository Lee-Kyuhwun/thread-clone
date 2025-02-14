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
import org.springframework.transaction.annotation.Transactional;
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



    @Transactional // 데이터의 정합성을 위해 트랜잭션 처리
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostPostRequestBody, UserEntity currentUser) {
        log.debug("current User : {}", currentUser.toString());
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );
        // 데이터 정합성을 위해
        ReplyEntity replyEntity = replyEntityRepository.save(
                ReplyEntity.of(replyPostPostRequestBody.body(), currentUser, postEntity)
        );
        // 댓글 갯수 증가
        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);
        postEntityRepository.save(postEntity);
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

    @Transactional // 데이터의 정합성을 위해 트랜잭션 처리
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId) // null인 경우 예외처리
                );
        ReplyEntity replyEntity = replyEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));
        if(!replyEntity.getUser().getUserId().equals(currentUser.getUserId())){
            throw new UserNotAllowedException();
        }
        replyEntityRepository.delete(replyEntity);
        // 댓글 갯수 감소

        // 0보다 작아지면 안되므로 Math 클래스의 max 메서드 사용 0과 비교하여 큰 값을 반환 0보다 작으면 0을 반환 0보다 크면 그 값을 반환
        postEntity.setRepliesCount(Math.max(0,postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }
}
