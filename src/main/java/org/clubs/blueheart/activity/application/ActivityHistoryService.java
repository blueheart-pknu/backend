package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.repository.ActivityHistoryRepository;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.activity.dto.request.ActivitySubscribeRequestDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityHistoryService {

    private final ActivityHistoryRepository activityHistoryRepository;

    public ActivityHistoryService(ActivityHistoryRepository activityHistoryRepository) {
        this.activityHistoryRepository = activityHistoryRepository;
    }
    public void subscribeActivity(ActivitySubscribeRequestDto activitySubscribeRequestDto) {
        activityHistoryRepository.subscribeActivityById(activitySubscribeRequestDto);
    }

    public void unsubscribeActivity(ActivitySubscribeRequestDto activitySubscribeRequestDto) {
        activityHistoryRepository.unsubscribeActivityById(activitySubscribeRequestDto);
    }


    public List<ActivitySearchResponseDto> getMyActivityHistoryInfo(Long id) {
        return activityHistoryRepository.getMyActivityHistoryInfoById(id);
    }

    public List<UserInfoResponseDto> findSubscribedUser(Long activityId) {
        return activityHistoryRepository.findSubscribedUserByActivityId(activityId);
    }
}
