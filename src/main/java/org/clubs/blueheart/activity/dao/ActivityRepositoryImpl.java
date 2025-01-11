package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.*;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {
    private final ActivityDao activityDao;
    private final UserDao userDao;

    public ActivityRepositoryImpl(UserDao userDao, ActivityDao activityDao) {
        this.userDao = userDao;
        this.activityDao = activityDao; // 생성자 주입
    }

    //TODO: 이중 생성의 경우 클라이언트 사이드에서 처리 해야 할듯
    //TODO: history 또한 생성
    @Override
    public void createActivity(ActivityCreateDto activityCreateDto) {
        if (activityCreateDto == null) {
            throw new IllegalArgumentException("ActivityCreateDto is null");
        }

        User creator = userDao.findById(activityCreateDto.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + activityCreateDto.getCreatorId()));

        Activity activity = Activity.builder()
                .creatorId(creator)
                .title(activityCreateDto.getTitle())
                .description(activityCreateDto.getDescription())
                .place(activityCreateDto.getPlace())
                .placeUrl(activityCreateDto.getPlaceUrl())
                .expiredAt(activityCreateDto.getExpiredAt())
                .build();

        activityDao.save(activity);
    }
    @Override
    public void updateActivityById(ActivityUpdateDto activityUpdateDto) {
        if (activityUpdateDto == null) {
            throw new IllegalArgumentException("activityUpdateDto is null");
        }

        // Fetch the user
        Activity existingActivity = activityDao.findActivityByIdAndDeletedAtIsNull(activityUpdateDto.getId())
                .orElseThrow(() -> new IllegalStateException("Activity not found"));

        // Update fields using updateFields
        Activity updatedActivity = existingActivity.toBuilder()
                .id(activityUpdateDto.getId() != null ? activityUpdateDto.getId() : existingActivity.getId())
                .title(activityUpdateDto.getTitle() != null ? activityUpdateDto.getTitle() : existingActivity.getTitle())
                .status(activityUpdateDto.getStatus() != null ? activityUpdateDto.getStatus() : existingActivity.getStatus())
                .maxNumber(activityUpdateDto.getMaxNumber() != null ? activityUpdateDto.getMaxNumber() : existingActivity.getMaxNumber())
                .description(activityUpdateDto.getDescription() != null ? activityUpdateDto.getDescription() : existingActivity.getDescription())
                .place(activityUpdateDto.getPlace() != null ? activityUpdateDto.getPlace() : existingActivity.getPlace())
                .placeUrl(activityUpdateDto.getPlaceUrl() != null ? activityUpdateDto.getPlaceUrl() : existingActivity.getPlaceUrl())
                .expiredAt(activityUpdateDto.getExpiredAt() != null ? activityUpdateDto.getExpiredAt() : existingActivity.getExpiredAt())
                .build();


        // Save the updated user
        activityDao.save(updatedActivity);

    }

    @Override
    public List<ActivitySearchDto> findActivityByStatus(ActivityStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status is null");
        }

        // Fetch activities by status
        List<Activity> activityInfo = activityDao.findActivityByStatus(status)
                .orElseThrow(() -> new IllegalStateException("Activities with status " + status + " not found"));

        // Map Activity entities to ActivitySearchDto
        return activityInfo.stream()
                .<ActivitySearchDto>map(activity -> ActivitySearchDto.builder()
                        .id(activity.getId())
                        .title(activity.getTitle())
                        .description(activity.getDescription())
                        .status(activity.getStatus())
                        .isSubscribed(false) // Set this field based on business logic
                        .currentNumber(activity.getUsers() != null ? activity.getUsers().size() : 0) // Handle null safety
                        .maxNumber(activity.getMaxNumber())
                        .expiredAt(activity.getExpiredAt())
                        .build())
                .toList();
    }

    @Override
    public List<ActivitySearchDto> findAllActivity() {
        // Fetch all activities that are not deleted
        List<Activity> activities = activityDao.findAllByDeletedAtIsNull();

        // Map to ActivitySearchDto
        return activities.stream()
                .<ActivitySearchDto>map(activity -> ActivitySearchDto.builder()
                        .id(activity.getId())
                        .title(activity.getTitle())
                        .description(activity.getDescription())
                        .status(activity.getStatus())
                        .isSubscribed(false) // Set this field based on business logic
                        .currentNumber(activity.getUsers() != null ? activity.getUsers().size() : 0) // Handle null safety
                        .maxNumber(activity.getMaxNumber())
                        .expiredAt(activity.getExpiredAt())
                        .build())
                .toList();
    }

    @Override
    public ActivityDetailDto findOneActivityDetailById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        // Fetch the activity by ID
        Activity activity = activityDao.findActivityByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalStateException("Activity with ID " + id + " not found"));

        // Map to ActivitySearchDto (assuming details can be extended)
        // Map to ActivityDetailDto
        return ActivityDetailDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .status(activity.getStatus())
                .isSubscribed(false) // Set this field based on business logic
                .currentNumber(activity.getUsers() != null ? activity.getUsers().size() : 0) // Handle null safety
                .maxNumber(activity.getMaxNumber())
                .expiredAt(activity.getExpiredAt())
                .place(activity.getPlace())
                .placeUrl(activity.getPlaceUrl())
                .build();
    }

    @Override
    public void deleteActivity(ActivityDeleteDto activityDeleteDto) {
        if (activityDeleteDto == null || activityDeleteDto.getId() == null) {
            throw new IllegalArgumentException("ActivityDeleteDto or its ID must not be null");
        }

        // Fetch the activity
        Activity existingActivity = activityDao.findActivityByIdAndDeletedAtIsNull(activityDeleteDto.getId())
                .orElseThrow(() -> new IllegalStateException("Activity not found"));

        // Mark as deleted using the builder
        Activity deletedActivity = existingActivity.toBuilder()
                .deletedAt(LocalDateTime.now()) // Set the deleted timestamp
                .build();

        // Save the updated activity
        activityDao.save(deletedActivity);
    }
}
