package com.fastcampus.thread.model.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Random;

@Entity
@Table(name = "\"user\"",
indexes = {@Index(name = "user_username_idx", columnList = "username",unique = true)}//유니크 인덱스로 설정하여 중복생성을 막음
)
@SQLDelete(sql = "UPDATE \"user\" SET deletedDateTime = current_timestamp WHERE userid = ?")
@Getter
@Setter
@EqualsAndHashCode
@SQLRestriction("deletedatetime IS NULL")
// SQLRestriction은 엔티티에 대해 쿼리시 자동으로 조건추가하도록한다.
// 즉 소프트 삭제처리가된 데이터가 조회되지 않도록 필터링 하는 용도로 사용
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column private String profile;

    @Column private String description;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserEntity of(String username, String password){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        //Avatar placeholder 서비스(https://avatars-placeholder.iran.liara.run)를 이용하여 랜덤 아바타 생성
        userEntity.setProfile("https://avatar.iran.liara.run/public/"+(new Random().nextInt(100)+1));
//        // 위에 터졌을 경우 아래로 대체
//        userEntity.setProfile(
//                "https://dev-jayce.github.io/public/profile/" + new Random().nextInt(100) + ".png"
//        );
        return userEntity;
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
