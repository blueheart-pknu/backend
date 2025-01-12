package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.dao.ActivityHistoryRepository;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.springframework.stereotype.Service;

@Service
public class ActivityHistoryService {

    private final ActivityHistoryRepository activityHistoryRepository;

    public ActivityHistoryService(ActivityHistoryRepository activityHistoryRepository) {
        this.activityHistoryRepository = activityHistoryRepository;
    }
    public void subscribeActivity(ActivitySubscribeDto activitySubscribeDto) {
        activityHistoryRepository.subscribeActivityById(activitySubscribeDto);
    }

    public void unsubscribeActivity(ActivitySubscribeDto activitySubscribeDto) {
        activityHistoryRepository.unsubscribeActivityById(activitySubscribeDto);
    }





}
