package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.UserSocialProfiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialProfilesSettingsRepository extends JpaRepository<UserSocialProfiles, Integer> {
}
