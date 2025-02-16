package com.fastcampus.thread.model.like;

import com.fastcampus.thread.model.post.PostEntity;
import com.fastcampus.thread.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "like",
indexes =  {
        @Index(name = "like_user_postid_idx",  columnList = "userid,postid",unique = true) // 유니크 인덱스
        // 유니크 인덱스는 중복을 허용하지 않는 인덱스
        // 유니크 인덱스를 사용하면 중복된 데이터를 저장하지 않도록 할 수 있음
        // userid, postid 두개의 컬럼을 조합하여 유니크 인덱스를 생성
}
)
@Getter
@Setter
@EqualsAndHashCode
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long likeId;



    @Column
    private ZonedDateTime createdDateTime;


    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private PostEntity post;

    // postEntity 초기화하는 로직은 UserEntity처럼 of 메서드로 실행
    public static LikeEntity of(UserEntity userEntity, PostEntity postEntity){
        var replyEntity = new LikeEntity();
        replyEntity.setUser(userEntity);
        replyEntity.setPost(postEntity);
        return replyEntity;
    }


    @PrePersist // 저장하기 전에 실행 ,JPA가 저장하기전에 실행
    private void prePersist(){
        this.createdDateTime = ZonedDateTime.now();
    }

}
