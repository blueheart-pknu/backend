package org.clubs.blueheart.group.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.OffsetDateTime;

import org.clubs.blueheart.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "groups")  // DB의 'users' 테이블과 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 20)
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    // ManyToMany 관계 설정
    @ManyToMany
    @JoinTable(
            name = "group_users",               // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "group_id"),   // groups 테이블 FK
            inverseJoinColumns = @JoinColumn(name = "user_id") // users 테이블 FK
    )
    private java.util.Set<User> users = new java.util.HashSet<>();

    @Builder
    public Group(String name) {
        this.name = name;
    }
}