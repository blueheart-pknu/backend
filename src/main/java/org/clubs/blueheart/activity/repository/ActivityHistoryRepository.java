package org.clubs.blueheart.activity.repository;

import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.activity.dto.request.ActivitySubscribeRequestDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;

import java.util.List;

public interface ActivityHistoryRepository {
    void subscribeActivityById(ActivitySubscribeRequestDto activitySubscribeRequestDto);

    void unsubscribeActivityById(ActivitySubscribeRequestDto activitySubscribeRequestDto);

    List<ActivitySearchResponseDto> getMyActivityHistoryInfoById(Long id);

    List<UserInfoResponseDto> findSubscribedUserByActivityId(Long activityId);
}
