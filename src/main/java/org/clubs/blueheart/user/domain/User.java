package org.clubs.blueheart.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.OffsetDateTime;

import org.clubs.blueheart.group.domain.Group;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "users")  // DB의 'users' 테이블과 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 10)
    private String username;

    @NotNull
    @Column(name = "student_number", nullable = false, unique = true)
    private Integer studentNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    // ManyToMany 반대편
    @ManyToMany(mappedBy = "users")
    private java.util.Set<Group> groups = new java.util.HashSet<>();

    @ManyToMany(mappedBy = "users")
    private java.util.Set<Group> activities = new java.util.HashSet<>();


    @Builder
    public User(String username, Integer studentNumber, UserRole role) {
        this.username = username;
        this.studentNumber = studentNumber;
        this.role = role;
    }
}