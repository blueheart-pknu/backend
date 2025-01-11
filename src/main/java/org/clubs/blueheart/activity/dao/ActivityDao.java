package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ActivityDao extends ActivityCustomDao, JpaRepository<Activity, Long> {
    Optional<Activity> findActivityByIdAndDeletedAtIsNull(Long id);

    Optional<List<Activity>> findActivityByStatus(ActivityStatus status);

    List<Activity> findAllByDeletedAtIsNull();
}