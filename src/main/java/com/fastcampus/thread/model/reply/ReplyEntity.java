package com.fastcampus.thread.model.reply;

import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Entity
@Table(name = "reply",
indexes =  {
        @Index(name = "reply_userid_idx",  columnList = "userid"),
        @Index(name = "reply_postid_idx",  columnList = "postid")
}
)
@Getter
@Setter
@EqualsAndHashCode
@SQLDelete(sql = "UPDATE post SET deletedDateTime = current_timestamp WHERE id = ?") // deletedDateTime에 현재 시간을 넣어줌
@SQLRestriction("deletedDateTime is null") // deletedDateTime이 null인 경우만 조회 즉 삭제 X
// @SQLRestriction은 @SQLDelete와 같이 사용해야함
//@SQLDelete와 @SQLRestriction은 논리적 삭제를 위한 어노테이션
// SQLDELETE는 삭제 시간을 기록하는 역할
// SQLRestriction은 삭제되지 않은 데이터만 조회하는 역할
// 논리적 삭제는 데이터를 삭제하지 않고 삭제 여부를 표시하는 방식
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long replyId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column

    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;



    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    // postEntity 초기화하는 로직은 UserEntity처럼 of 메서드로 실행
    public static ReplyEntity of(String body, UserEntity userEntity,PostEntity postEntity){
        var replyEntity = new ReplyEntity();
        replyEntity.setBody(body);
        replyEntity.setUser(userEntity);
        replyEntity.setPost(postEntity);
        return replyEntity;
    }


    @PrePersist // 저장하기 전에 실행 ,JPA가 저장하기전에 실행
    private void prePersist(){
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate // 업데이트 하기 전에 실행
    private void preUpdate(){
        this.updatedDateTime = ZonedDateTime.now();
    }

}
