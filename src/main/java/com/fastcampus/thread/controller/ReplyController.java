package com.fastcampus.thread.controller;


import com.fastcampus.thread.model.reply.Reply;
import com.fastcampus.thread.model.reply.ReplyPatchRequestBody;
import com.fastcampus.thread.model.reply.ReplyPostRequestBody;
import com.fastcampus.thread.model.user.UserEntity;
import com.fastcampus.thread.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")// 모든 댓글은 특정 게시글에 속해있음
@Slf4j
public class ReplyController {



    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable("postId") Long postId){
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }



    @PostMapping
    public ResponseEntity<Reply> createReply(

            @RequestParam("postId") Long postId
            ,@RequestBody ReplyPostRequestBody replyPostRequestBody,
                                           Authentication authentication){
        /**
         * principal에서 userDetail을 넣어줌
         * principal값에다가 토큰을 통해 가져온 userDetails 정보를 넣어줬고
         * 이렇게 넣어준 사용자 정보를 Authentication을 통해서 가져올 수 있다.
         * */
        Object principal = authentication.getPrincipal();
        var reply = replyService.createReply(postId,replyPostRequestBody,(UserEntity) principal);
        return ResponseEntity.ok(reply);
    }
    // ReuqestBody 정상동작하려면 PostPostRequestBody에 기본생성자가 있어야함

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updatePost(@RequestBody ReplyPatchRequestBody replyPatchRequestBody,
                                           @PathVariable("replyId") Long replyId,
                                             @PathVariable("postId") Long postId,
                                           Authentication authentication){
        Object principal = authentication.getPrincipal();
        var reply = replyService.updateReply(replyPatchRequestBody, postId,replyId,(UserEntity) principal);
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deletePost(@PathVariable("replyId") Long replyId,
                                           @PathVariable("postId") Long postId,
                                           Authentication authentication){
        Object principal = authentication.getPrincipal();
        replyService.deleteReply(postId,replyId,(UserEntity) principal);
        return ResponseEntity.noContent().build(); // 204
    }


}
