package org.clubs.blueheart.activity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.clubs.blueheart.user.domain.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ActivityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Builder
    public ActivityHistory(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }
}