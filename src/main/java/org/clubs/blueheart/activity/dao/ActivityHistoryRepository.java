package org.clubs.blueheart.activity.dao;


import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;

public interface ActivityHistoryRepository {
    void subscribeActivityById(ActivitySubscribeDto activitySubscribeDto);

    void unsubscribeActivityById(ActivitySubscribeDto activitySubscribeDto);
}
