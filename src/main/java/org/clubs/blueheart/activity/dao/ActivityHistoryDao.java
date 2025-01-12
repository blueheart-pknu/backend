package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityHistoryDao extends ActivityCustomDao, JpaRepository<ActivityHistory, Long> {
    boolean existsByActivityIdAndUserIdAndDeletedAtIsNull(Long activityId, Long userId);

    Optional<ActivityHistory> findByActivityIdAndUserIdAndDeletedAtIsNull(Long activityId, Long userId);

    List<ActivityHistory> findAllByActivity_IdAndDeletedAtIsNull(Long receiverId);
}
