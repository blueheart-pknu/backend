package org.clubs.blueheart.notification.dao;

import org.clubs.blueheart.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDao extends JpaRepository<Notification, Long> {

}
