package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    @Override
    public List<ActivitySearchDto> findOneActivityDetailById(Long id) {
        return List.of();
    }
}
