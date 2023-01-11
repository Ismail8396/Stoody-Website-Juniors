package com.loam.stoody.repository.communication.notification;

import com.loam.stoody.model.communication.notification.SimpleNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleNotificationRepository extends JpaRepository<SimpleNotification,Long> {
}
