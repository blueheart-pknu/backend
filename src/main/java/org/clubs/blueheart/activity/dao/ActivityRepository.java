package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.*;

import java.util.List;

public interface ActivityRepository {
    void updateActivityById(ActivityUpdateDto activityUpdateDto);

    List<ActivitySearchDto> findActivityByStatus(ActivityStatus status);

    List<ActivitySearchDto> findAllActivity();

    ActivityDetailDto findOneActivityDetailById(Long id);

    void createActivity(ActivityCreateDto activityCreateDto);

    void deleteActivity(ActivityDeleteDto activityDeleteDto);

}
