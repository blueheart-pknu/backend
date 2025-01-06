package org.clubs.blueheart.notifications.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.OffsetDateTime;

import org.clubs.blueheart.users.domain.User;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "notifications")  // DB의 'users' 테이블과 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //**optional = false**라면, 해당 엔티티가 항상 연관된 대상(여기서는 User)을 반드시 가져야 한다는 의미입니다.

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User senderId;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverId;

    @NotNull
    @Column(nullable = false, length = 255)
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Builder
    public Notification(User senderId, User receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
}