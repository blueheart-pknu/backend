package org.clubs.blueheart.activity.repository;

import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;

import java.util.List;

public interface ActivityRepository {
    void updateActivityById(ActivityUpdateRequestDto activityUpdateRequestDto);

    List<ActivitySearchResponseDto> findActivityByStatus(ActivityStatus status);

    List<ActivitySearchResponseDto> findAllActivity();

    ActivityDetailResponseDto findOneActivityDetailById(Long id);

    void createActivity(ActivityCreateRequestDto activityCreateRequestDto);

    void deleteActivity(ActivityDeleteRequestDto activityDeleteRequestDto);

}
