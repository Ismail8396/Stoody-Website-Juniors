package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileSettingsRepository extends JpaRepository<UserProfile, Integer> {
}
