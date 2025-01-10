package org.clubs.blueheart.notification.application;

import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.clubs.blueheart.notification.dao.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void subscribeActivity(ActivitySubscribeDto activitySubscribeDto) {
    }

    public void unsubscribeActivity(ActivitySubscribeDto activitySubscribeDto) {
    }

    public List<ActivitySearchDto> findOneActivityDetailById(Long id) {
        return notificationRepository.findOneActivityDetailById(id);
    }
}
