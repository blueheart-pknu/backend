package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.ActivitySearchDto;
import org.clubs.blueheart.activity.dto.ActivityUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {
    private final ActivityDao activityDao;

    public ActivityRepositoryImpl(ActivityDao activityDao) {
        this.activityDao = activityDao; // 생성자 주입
    }

    @Override
    public void updateActivityById(ActivityUpdateDto activityUpdateDto) {

    }

    @Override
    public List<ActivitySearchDto> findActivityByStatus(ActivityStatus status) {
        return List.of();
    }

    @Override
    public List<ActivitySearchDto> findAllActivity() {
        return List.of();
    }

    @Override
    public List<ActivitySearchDto> findOneActivityDetailById(Long id) {
        return List.of();
    }
}
