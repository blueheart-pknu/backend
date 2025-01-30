package org.clubs.blueheart.activity.repository;

import org.clubs.blueheart.activity.dao.ActivityHistoryDao;
import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.activity.dto.request.ActivitySubscribeRequestDto;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ActivityHistoryRepositoryImpl implements ActivityHistoryRepository{

    private final ActivityHistoryDao activityHistoryDao;

    public ActivityHistoryRepositoryImpl(ActivityHistoryDao activityHistoryDao) {
        this.activityHistoryDao = activityHistoryDao;
    }

    //TODO: 추후에 deletedAt이 null이 아닌 경우 다시 null로 바꾸기
    @Override
    public void subscribeActivityById(ActivitySubscribeRequestDto activitySubscribeRequestDto) {

        if (activitySubscribeRequestDto == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Check if the user is already subscribed to the activity
        boolean alreadySubscribed = activityHistoryDao.existsByActivityIdAndUserIdAndDeletedAtIsNull(
                activitySubscribeRequestDto.getActivityId(),
                activitySubscribeRequestDto.getUserId()
        );

        if (alreadySubscribed) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_HISTORY_ALREADY_SUBSCRIBED);
        }


        // Create a new ActivityHistory record
        ActivityHistory activityHistory = ActivityHistory.builder()
                .activity(Activity.builder().id(activitySubscribeRequestDto.getActivityId()).build()) // Use activity ID from DTO
                .user(User.builder().id(activitySubscribeRequestDto.getUserId()).build()) // Use user ID from DTO
                .build();

        // Save the ActivityHistory record
        activityHistoryDao.save(activityHistory);
    }

    @Override
    public void unsubscribeActivityById(ActivitySubscribeRequestDto activitySubscribeRequestDto) {

        if (activitySubscribeRequestDto == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch the existing ActivityHistory record
        ActivityHistory activityHistory = activityHistoryDao.findByActivityIdAndUserIdAndDeletedAtIsNull(
                activitySubscribeRequestDto.getActivityId(),
                activitySubscribeRequestDto.getUserId()
        ).orElseThrow(() -> new RepositoryException(ExceptionStatus.ACTIVITY_HISTORY_NOT_SUBSCRIBED));

        // Mark the ActivityHistory record as deleted
        ActivityHistory updatedActivityHistory = activityHistory.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        // Save the updated ActivityHistory record
        activityHistoryDao.save(updatedActivityHistory);
    }

    @Override
    public List<ActivitySearchResponseDto> getMyActivityHistoryInfoById(Long userId) {
        if (userId == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // Fetch the activity history for the given user where ActivityStatus is PROGRESSING or COMPLETED
        List<ActivityHistory> activityHistories = activityHistoryDao.findByUserIdAndActivityStatusInAndDeletedAtIsNull(
                userId,
                List.of(ActivityStatus.PROGRESSING, ActivityStatus.COMPLETED) // Filter by status
        );

        // Map ActivityHistory to ActivitySearchDto
        return activityHistories.stream()
                .map(activityHistory -> {
                    Activity activity = activityHistory.getActivity();
                    return ActivitySearchResponseDto.builder()
                            .activityId(activity.getId()) // Use `id` field instead of `activityId`
                            .title(activity.getTitle())
                            .place(activity.getPlace())
                            .isSubscribed(true)
                            .status(activity.getStatus())
                            .maxNumber(activity.getMaxNumber())
                            .currentNumber(activityHistoryDao.countByActivityId(activity.getId())) // Fetch count of subscriptions
                            .expiredAt(activity.getExpiredAt())
                            .build();
                })
                .collect(Collectors.toList()); // Collect stream into a list
    }

    @Override
    public List<UserInfoResponseDto> findSubscribedUserByActivityId(Long activityId) {
        if (activityId == null) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_INVALID_PARAMS);
        }

        // activityId와 deletedAt이 null인 ActivityHistory 레코드 조회
        List<ActivityHistory> activityHistories = activityHistoryDao.findByActivityIdAndDeletedAtIsNull(activityId);

        // 구독한 사용자가 없는 경우 예외 처리 (필요 시)
        if (activityHistories.isEmpty()) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_HISTORY_NOT_SUBSCRIBED);
        }

        // ActivityHistory에서 User를 추출하고 UserInfoDto로 매핑
        return activityHistories.stream()
                .map(ActivityHistory::getUser)
                .map(user -> UserInfoResponseDto.builder()
                        .username(user.getUsername())
                        .studentNumber(user.getStudentNumber())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }

}