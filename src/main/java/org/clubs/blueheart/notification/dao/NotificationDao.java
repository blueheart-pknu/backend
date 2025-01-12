package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationDao extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverId_IdAndDeletedAtIsNull(Long receiverId);
}
