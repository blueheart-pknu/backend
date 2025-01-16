package org.clubs.blueheart.notification.repository;

import org.clubs.blueheart.notification.dto.request.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.response.NotificationResponseDto;

import java.util.List;

public interface NotificationRepository {
    void createActivityNotification(NotificationRequestDto notificationRequestDto);

    void createGroupNotification(NotificationRequestDto notificationRequestDto);

    List<NotificationResponseDto> findAllNotificationMe(Long id);
}
