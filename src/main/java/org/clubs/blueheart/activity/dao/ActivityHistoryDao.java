package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityHistoryDao extends ActivityCustomDao, JpaRepository<ActivityHistory, Long> {
}
