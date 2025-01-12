package org.clubs.blueheart.activity.dao;


import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;

import java.util.List;

public interface ActivityHistoryRepository {
    void subscribeActivityById(ActivitySubscribeDto activitySubscribeDto);

    void unsubscribeActivityById(ActivitySubscribeDto activitySubscribeDto);


}
