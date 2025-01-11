package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.activity.dao.ActivityHistoryDao;
import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.group.dao.GroupUserDao;
import org.clubs.blueheart.group.domain.GroupUser;
import org.clubs.blueheart.notification.domain.Notification;
import org.clubs.blueheart.notification.dto.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.NotificationResponseDto;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private NotificationDao notificationDao;
    private UserDao userDao;
    private ActivityHistoryDao activityHistoryDao;
    private GroupUserDao groupUserDao;

    public NotificationRepositoryImpl(NotificationDao notificationDao, UserDao userdao, ActivityHistoryDao activityHistoryDao, GroupUserDao groupUserDao) {
        this.notificationDao = notificationDao;
        this.userDao = userdao;
        this.activityHistoryDao = activityHistoryDao;
        this.groupUserDao = groupUserDao;
    }

    @Override
    public void createActivityNotification(NotificationRequestDto notificationRequestDto) {
        if (notificationRequestDto == null || notificationRequestDto.getSenderId() == null || notificationRequestDto.getReceiverId() == null) {
            throw new IllegalArgumentException("NotificationRequestDto or its fields cannot be null");
        }

        // Fetch sender User entity
        User sender = userDao.findById(notificationRequestDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + notificationRequestDto.getSenderId()));

        // Fetch all ActivityHistory entries for the given Activity ID (ReceiverId is treated as ActivityId)
        List<ActivityHistory> activityHistories = activityHistoryDao.findAllByActivity_IdAndDeletedAtIsNull(notificationRequestDto.getReceiverId());

        if (activityHistories.isEmpty()) {
            throw new IllegalStateException("No users found for Activity ID: " + notificationRequestDto.getReceiverId());
        }

        // Extract users from ActivityHistory
        List<User> usersInActivity = activityHistories.stream()
                .map(ActivityHistory::getUser)
                .toList();

        // Create notifications for all users in the activity
        List<Notification> notifications = usersInActivity.stream()
                .map(user -> Notification.builder()
                        .senderId(sender)
                        .receiverId(user)
                        .content(notificationRequestDto.getContent())
                        .build())
                .toList();

        // Save all notifications
        notificationDao.saveAll(notifications);
    }

    @Override
    public void createGroupNotification(NotificationRequestDto notificationRequestDto) {
        if (notificationRequestDto == null || notificationRequestDto.getSenderId() == null || notificationRequestDto.getReceiverId() == null) {
            throw new IllegalArgumentException("NotificationRequestDto or its fields cannot be null");
        }

        // Fetch sender User entity
        User sender = userDao.findById(notificationRequestDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + notificationRequestDto.getSenderId()));

        // Fetch all GroupUser entries for the given Group ID (ReceiverId is treated as GroupId)
        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndDeletedAtIsNull(notificationRequestDto.getReceiverId());

        if (groupUsers.isEmpty()) {
            throw new IllegalStateException("No users found for Group ID: " + notificationRequestDto.getReceiverId());
        }

        // Extract User entities from GroupUser
        List<User> usersInGroup = groupUsers.stream()
                .map(GroupUser::getUser) // Extract User from GroupUser
                .toList();

        // Create notifications for all users in the group
        List<Notification> notifications = usersInGroup.stream()
                .map(user -> Notification.builder()
                        .senderId(sender)
                        .receiverId(user) // Set the receiver
                        .content(notificationRequestDto.getContent())
                        .build())
                .toList();

        // Save all notifications
        notificationDao.saveAll(notifications);
    }

    @Override
    public List<NotificationResponseDto> findAllNotification(Long receiverId) {
        if (receiverId == null) {
            throw new IllegalArgumentException("Receiver ID cannot be null");
        }

        List<Notification> notifications = notificationDao.findAllByReceiverId_IdAndDeletedAtIsNull(receiverId);

        if (notifications.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        // Update deletedAt field for all notifications using builder pattern
        List<Notification> updatedNotifications = notifications.stream()
                .map(notification -> notification.toBuilder()
                        .deletedAt(LocalDateTime.now())
                        .build())
                .toList();

        // Save updated notifications
        notificationDao.saveAll(updatedNotifications);

        // Map to NotificationResponseDto
        return updatedNotifications.stream()
                .map(notification -> NotificationResponseDto.builder()
                        .content(notification.getContent())
                        .senderUsername(notification.getSenderId().getUsername()) // Assuming User entity has `username` field
                        .createdAt(notification.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
