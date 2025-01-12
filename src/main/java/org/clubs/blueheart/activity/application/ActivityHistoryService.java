package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.dao.ActivityHistoryRepository;
import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<ActivitySearchDto> getMyActivityHistoryInfo(Long id) {
        return activityHistoryRepository.getMyActivityHistoryInfoById(id);
    }

    public List<UserInfoDto> findSubscribedUser(Long activityId) {
        return activityHistoryRepository.findSubscribedUserByActivityId(activityId);
    }
}
