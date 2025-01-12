package org.clubs.blueheart.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

import org.clubs.blueheart.group.domain.Group;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")  // DB의 'users' 테이블과 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false, length = 10)
    private String username;
    @NotNull
    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Builder(toBuilder = true)
    private User(Long id, String username, String studentNumber, UserRole role, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.studentNumber = studentNumber;
        this.role = role;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.deletedAt = deletedAt;
    }

    public User updatedUserFields(String username, String studentNumber, UserRole role) {
        return this.toBuilder()
                .username(username != null ? username : this.username)
                .studentNumber(studentNumber != null ? studentNumber : this.studentNumber)
                .role(role != null ? role : this.role)
                .build();
    }
}