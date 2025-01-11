package org.clubs.blueheart.notification.application;

import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.clubs.blueheart.notification.dao.NotificationRepository;
import org.clubs.blueheart.notification.dto.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.NotificationResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notificationActivity(NotificationRequestDto notificationRequestDto) {
        notificationRepository.createActivityNotification(notificationRequestDto);
    }

    public void notificationGroup(NotificationRequestDto notificationRequestDto) {
        notificationRepository.createGroupNotification(notificationRequestDto);
    }

    public List<NotificationResponseDto> findAllNotificationMe(Long id) {
        return notificationRepository.findAllNotificationMe(id);
    }
}
