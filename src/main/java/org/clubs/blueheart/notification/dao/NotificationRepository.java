package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.notification.dto.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationRepository {
    void createActivityNotification(NotificationRequestDto notificationRequestDto);

    void createGroupNotification(NotificationRequestDto notificationRequestDto);

    List<NotificationResponseDto> findAllNotification(Long id);
}
