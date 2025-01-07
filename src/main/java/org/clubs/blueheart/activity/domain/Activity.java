package org.clubs.blueheart.activity.domain;

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
@Table(name = "activities")  // DB의 'users' 테이블과 매핑
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name="creatorId", nullable = false)
    private User creator_id;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @Column(name="max_number")
    private Integer maxNumber;

    @NotNull
    @Column(nullable = false, length = 255)
    private String description;

    @NotNull
    @Column(nullable = false, length = 255)
    private String place;

    @NotNull
    @Column(name="place_url", nullable = false, length = 255)
    private String placeUrl;

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
            name = "activity_histories",               // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "activity_id"),   // groups 테이블 FK
            inverseJoinColumns = @JoinColumn(name = "user_id") // users 테이블 FK
    )
    private java.util.Set<User> users = new java.util.HashSet<>();

    @Builder
    public Activity(ActivityStatus status, Integer maxNumber, String description, String place, String placeUrl) {
        this.status = status;
        this.maxNumber = maxNumber;
        this.description = description;
        this.place = place;
        this.placeUrl = placeUrl;
    }
}