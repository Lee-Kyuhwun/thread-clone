package com.fastcampus.thread.model.post;

import com.fastcampus.thread.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Entity
@Table(name = "post",
indexes =  {
        @Index(name = "post_userid_idx",  columnList = "userid")
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
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long postId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column

    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    // TODO: 게시물을 작성한 유저정보
    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;



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
