package org.clubs.blueheart.activity.application;

import org.clubs.blueheart.activity.dao.ActivityRepository;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.ActivityCreateDto;
import org.clubs.blueheart.activity.dto.ActivityDeleteDto;
import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivityUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    public void updateActivityById(ActivityUpdateDto activityUpdateDto) {
        activityRepository.updateActivityById(activityUpdateDto);
    }

    public List<ActivitySearchDto> findActivityByStatus(ActivityStatus status) {
        return activityRepository.findActivityByStatus(status);
    }

    public List<ActivitySearchDto> findAllActivity() {
        return activityRepository.findAllActivity();
    }

    public List<ActivitySearchDto> findOneActivityDetailById(Long id) {
        return activityRepository.findOneActivityDetailById(id);
    }

    public void deleteActivity(ActivityDeleteDto activityDeleteDto) {
    }

    public void createActivity(ActivityCreateDto activityCreateDto) {

    }
}

