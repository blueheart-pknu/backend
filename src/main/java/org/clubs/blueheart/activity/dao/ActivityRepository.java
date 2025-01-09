package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivityUpdateDto;

import java.util.List;

public interface ActivityRepository {
    void updateActivityById(ActivityUpdateDto activityUpdateDto);

    List<ActivitySearchDto> findActivityByStatus(ActivityStatus status);

    List<ActivitySearchDto> findAllActivity();

    List<ActivitySearchDto> findOneActivityDetailById(Long id);
}
