package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.activity.dto.ActivitySubscribeDto;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ActivityHistoryRepositoryImpl implements ActivityHistoryRepository{

    private ActivityHistoryDao activityHistoryDao;

    public ActivityHistoryRepositoryImpl(ActivityHistoryDao activityHistoryDao) {
        this.activityHistoryDao = activityHistoryDao;
    }

    //TODO: 추후에 deletedAt이 null이 아닌 경우 다시 null로 바꾸기
    @Override
    public void subscribeActivityById(ActivitySubscribeDto activitySubscribeDto) {

        if (activitySubscribeDto == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        // Check if the user is already subscribed to the activity
        boolean alreadySubscribed = activityHistoryDao.existsByActivityIdAndUserIdAndDeletedAtIsNull(
                activitySubscribeDto.getActivityId(),
                activitySubscribeDto.getUserId()
        );

        if (alreadySubscribed) {
            throw new RepositoryException(ExceptionStatus.ACTIVITY_HISTORY_ALREADY_SUBSCRIBED);
        }


        // Create a new ActivityHistory record
        ActivityHistory activityHistory = ActivityHistory.builder()
                .activity(Activity.builder().id(activitySubscribeDto.getActivityId()).build()) // Use activity ID from DTO
                .user(User.builder().id(activitySubscribeDto.getUserId()).build()) // Use user ID from DTO
                .build();

        // Save the ActivityHistory record
        activityHistoryDao.save(activityHistory);
    }

    @Override
    public void unsubscribeActivityById(ActivitySubscribeDto activitySubscribeDto) {

        if (activitySubscribeDto == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        // Fetch the existing ActivityHistory record
        ActivityHistory activityHistory = activityHistoryDao.findByActivityIdAndUserIdAndDeletedAtIsNull(
                activitySubscribeDto.getActivityId(),
                activitySubscribeDto.getUserId()
        ).orElseThrow(() -> new RepositoryException(ExceptionStatus.ACTIVITY_HISTORY_NOT_SUBSCRIBED));

        // Mark the ActivityHistory record as deleted
        ActivityHistory updatedActivityHistory = activityHistory.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        // Save the updated ActivityHistory record
        activityHistoryDao.save(updatedActivityHistory);
    }

}