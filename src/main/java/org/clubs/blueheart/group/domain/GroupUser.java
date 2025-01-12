package org.clubs.blueheart.group.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.clubs.blueheart.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_users") // 중간 테이블 이름
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class GroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false) // groups 테이블 FK
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // users 테이블 FK
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder(toBuilder = true)
    public GroupUser(Long id, Group group, User user, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.group = group;
        this.user = user;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.deletedAt = deletedAt;
    }
}