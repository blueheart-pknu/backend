package org.clubs.blueheart.activity.dao;


import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.clubs.blueheart.user.dto.UserInfoDto;

import java.util.List;

public interface ActivityHistoryRepository {
    void subscribeActivityById(ActivitySubscribeDto activitySubscribeDto);

    void unsubscribeActivityById(ActivitySubscribeDto activitySubscribeDto);


    List<ActivitySearchDto> getMyActivityHistoryInfoById(Long id);

    List<UserInfoDto> findSubscribedUserByActivityId(Long activityId);
}
