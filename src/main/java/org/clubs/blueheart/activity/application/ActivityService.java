package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.clubs.blueheart.activity.repository.ActivityRepository;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void createActivity(ActivityCreateRequestDto activityCreateRequestDto) {
        activityRepository.createActivity(activityCreateRequestDto);
    }

    public void updateActivity(ActivityUpdateRequestDto activityUpdateRequestDto) {
        activityRepository.updateActivityById(activityUpdateRequestDto);
    }

    @Transactional(readOnly = true)
    public List<ActivitySearchResponseDto> findActivityByStatus(ActivityStatus status) {
        return activityRepository.findActivityByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<ActivitySearchResponseDto> findAllActivity() {
        return activityRepository.findAllActivity();
    }

    @Transactional(readOnly = true)
    public ActivityDetailResponseDto findOneActivityDetailById(Long id) {
        return activityRepository.findOneActivityDetailById(id);
    }

    public void deleteActivity(ActivityDeleteRequestDto activityDeleteRequestDto) {
        activityRepository.deleteActivity(activityDeleteRequestDto);
    }

}

