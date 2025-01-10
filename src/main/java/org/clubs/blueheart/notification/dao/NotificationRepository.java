package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.activity.dto.ActivitySearchDto;

import java.util.List;

public interface NotificationRepository {
    List<ActivitySearchDto> findOneActivityDetailById(Long id);
}
