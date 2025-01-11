package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.dto.*;
import org.springframework.transaction.annotation.Transactional;
import org.clubs.blueheart.activity.dao.ActivityRepository;
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

    public void createActivity(ActivityCreateDto activityCreateDto) {
        activityRepository.createActivity(activityCreateDto);
    }

    public void updateActivity(ActivityUpdateDto activityUpdateDto) {
        activityRepository.updateActivityById(activityUpdateDto);
    }

    @Transactional(readOnly = true)
    public List<ActivitySearchDto> findActivityByStatus(ActivityStatus status) {
        return activityRepository.findActivityByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<ActivitySearchDto> findAllActivity() {
        return activityRepository.findAllActivity();
    }

    @Transactional(readOnly = true)
    public ActivityDetailDto findOneActivityDetailById(Long id) {
        return activityRepository.findOneActivityDetailById(id);
    }

    public void deleteActivity(ActivityDeleteDto activityDeleteDto) {
        activityRepository.deleteActivity(activityDeleteDto);
    }

}

