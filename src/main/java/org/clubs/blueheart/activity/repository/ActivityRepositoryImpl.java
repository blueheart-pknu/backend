package org.clubs.blueheart.activity.repository;

import org.clubs.blueheart.activity.dao.ActivityDao;
import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
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
        this.activityDao = activityDao;
    }

    //TODO: 이중 생성의 경우 클라이언트 사이드에서 처리 해야 할듯
    //TODO: history 또한 생성
    @Override
    public void createActivity(ActivityCreateRequestDto activityCreateRequestDto) {
        if (activityCreateRequestDto == null) {
            throw new IllegalArgumentException("ActivityCreateDto is null");
        }

        User creator = userDao.findById(activityCreateRequestDto.getCreatorId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        Activity activity = Activity.builder()
                .creatorId(creator)
                .title(activityCreateRequestDto.getTitle())
                .status(ActivityStatus.PROGRESSING)
                .description(activityCreateRequestDto.getDescription())
                .maxNumber(activityCreateRequestDto.getMaxNumber())
                .place(activityCreateRequestDto.getPlace())
                .placeUrl(activityCreateRequestDto.getPlaceUrl())
                .expiredAt(activityCreateRequestDto.getExpiredAt())
                .build();

        activityDao.save(activity);
    }
    @Override
    public void updateActivityById(ActivityUpdateRequestDto activityUpdateRequestDto) {
        if (activityUpdateRequestDto == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch the user
        Activity existingActivity = activityDao.findActivityByIdAndDeletedAtIsNull(activityUpdateRequestDto.getActivityId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // Update fields using updateFields
        Activity updatedActivity = existingActivity.toBuilder()
                .id(activityUpdateRequestDto.getActivityId() != null ? activityUpdateRequestDto.getActivityId() : existingActivity.getId())
                .title(activityUpdateRequestDto.getTitle() != null ? activityUpdateRequestDto.getTitle() : existingActivity.getTitle())
                .status(activityUpdateRequestDto.getStatus() != null ? activityUpdateRequestDto.getStatus() : existingActivity.getStatus())
                .maxNumber(activityUpdateRequestDto.getMaxNumber() != null ? activityUpdateRequestDto.getMaxNumber() : existingActivity.getMaxNumber())
                .description(activityUpdateRequestDto.getDescription() != null ? activityUpdateRequestDto.getDescription() : existingActivity.getDescription())
                .place(activityUpdateRequestDto.getPlace() != null ? activityUpdateRequestDto.getPlace() : existingActivity.getPlace())
                .placeUrl(activityUpdateRequestDto.getPlaceUrl() != null ? activityUpdateRequestDto.getPlaceUrl() : existingActivity.getPlaceUrl())
                .expiredAt(activityUpdateRequestDto.getExpiredAt() != null ? activityUpdateRequestDto.getExpiredAt() : existingActivity.getExpiredAt())
                .build();


        // Save the updated user
        activityDao.save(updatedActivity);

    }

    @Override
    public List<ActivitySearchResponseDto> findActivityByStatus(ActivityStatus status) {
        if (status == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch activities by status
        List<Activity> activityInfo = activityDao.findActivityByStatus(status)
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND));

        // Map Activity entities to ActivitySearchDto
        return activityInfo.stream()
                .<ActivitySearchResponseDto>map(activity -> ActivitySearchResponseDto.builder()
                        .activityId(activity.getId())
                        .title(activity.getTitle())
                        .status(activity.getStatus())
                        .isSubscribed(false) // Set this field based on business logic
                        .place(activity.getPlace())
                        .currentNumber(activity.getUsers() != null ? activity.getUsers().size() : 0) // Handle null safety
                        .maxNumber(activity.getMaxNumber())
                        .expiredAt(activity.getExpiredAt())
                        .build())
                .toList();
    }

    @Override
    public List<ActivitySearchResponseDto> findAllActivity() {
        // Fetch all activities that are not deleted
        List<Activity> activities = activityDao.findAllByDeletedAtIsNull();

        // Map to ActivitySearchDto
        return activities.stream()
                .<ActivitySearchResponseDto>map(activity -> ActivitySearchResponseDto.builder()
                        .activityId(activity.getId())
                        .title(activity.getTitle())
                        .status(activity.getStatus())
                        .isSubscribed(false) // Set this field based on business logic
                        .place(activity.getPlace())
                        .currentNumber(activity.getUsers() != null ? activity.getUsers().size() : 0) // Handle null safety
                        .maxNumber(activity.getMaxNumber())
                        .expiredAt(activity.getExpiredAt())
                        .build())
                .toList();
    }

    @Override
    public ActivityDetailResponseDto findOneActivityDetailById(Long id) {
        if (id == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch the activity by ID
        Activity activity = activityDao.findActivityByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND));

        // Map to ActivitySearchDto (assuming details can be extended)
        // Map to ActivityDetailDto
        return ActivityDetailResponseDto.builder()
                .activityId(activity.getId())
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
    public void deleteActivity(ActivityDeleteRequestDto activityDeleteRequestDto) {
        if (activityDeleteRequestDto == null || activityDeleteRequestDto.getActivityId() == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch the activity
        Activity existingActivity = activityDao.findActivityByIdAndDeletedAtIsNull(activityDeleteRequestDto.getActivityId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND));

        // Mark as deleted using the builder
        Activity deletedActivity = existingActivity.toBuilder()
                .deletedAt(LocalDateTime.now()) // Set the deleted timestamp
                .build();

        // Save the updated activity
        activityDao.save(deletedActivity);
    }
}
