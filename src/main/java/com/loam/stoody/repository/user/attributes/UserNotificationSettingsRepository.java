package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.UserNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationSettingsRepository extends JpaRepository<UserNotifications, Integer> {
}
