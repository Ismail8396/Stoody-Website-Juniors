package com.loam.stoody.service.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.model.user.*;
import com.loam.stoody.model.user.misc.UserStatus;
import com.loam.stoody.model.user.statistics.UserStatistics;
import com.loam.stoody.repository.user.attributes.UserProfileSettingsRepository;
import com.loam.stoody.service.communication.report.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Data Transfer Service
@Service
@AllArgsConstructor
public class UserDTS {
    private final CustomUserDetailsService customUserDetailsService;
    private final IAuthenticationFacade authenticationFacade;
    private final ReportService reportService;
    private final UserProfileSettingsRepository userProfileSettingsRepository;

    public User getCurrentUser() {
        return customUserDetailsService.getCurrentUser();
    }

    public User getUserByUsername(String username) {
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(username);
        } catch (UsernameNotFoundException ignore) {
            return null;
        }
        return user;
    }

    public String getUserDisplayNameByUsername(String username) {
        UserProfile userProfile = getUserProfile(username);
        if (userProfile == null)
            if (userProfile.getFirstName() != null && userProfile.getLastName() != null)
                if (!userProfile.getFirstName().isBlank() && !userProfile.getLastName().isBlank())
                    return userProfile.getFirstName() + " " + userProfile.getLastName();
        User user = getUserByUsername(username);
        if (user != null)
            return user.getUsername();
        return "NULL";
    }

    public String getUserStatusCSSClass(String username) {
        User user = getUserByUsername(username);
        if(user == null)
            return "avatar-indicators avatar-offline";
        List<UserProfile> userProfiles = userProfileSettingsRepository.findAll().stream().filter(e->e.getUser().getUsername().equals(user.getUsername())).collect(Collectors.toList());
        if(userProfiles.isEmpty())
            return "avatar-indicators avatar-offline";

        String userStatus = userProfiles.get(0).getUserStatus();

        if (userStatus.equals(UserStatus.Online)) {
            return "avatar-indicators avatar-online";
        } else if (userStatus.equals(UserStatus.Offline)) {
            return "avatar-indicators avatar-offline";
        } else if (userStatus.equals(UserStatus.Away)) {
            return "avatar-indicators avatar-away";
        } else if (userStatus.equals(UserStatus.Busy)) {
            return "avatar-indicators avatar-busy";
        } else {
            return "avatar-indicators avatar-offline";
        }
    }

    // -> Followers / Following
    public List<User> getUserFollowersByUsername(String username) {
        return customUserDetailsService.getUserFollowers(customUserDetailsService.getUserByUsername(username));
    }

    public List<User> getUserFollowingsByUsername(String username) {
        return customUserDetailsService.getUserFollowings(customUserDetailsService.getUserByUsername(username));
    }

    public Boolean isUserFollowedByMe(String userFrom,
                                      String userTo) {
        return customUserDetailsService.isUserFollowedBy(customUserDetailsService.getUserByUsername(userFrom),
                customUserDetailsService.getUserByUsername(userTo));
    }

    // -> Report User
    public Boolean didIReportThisUser(String reportedBy, String reportedUser) {
        return reportService.doesReportedUserExistByUsername(reportedBy, reportedUser);
    }

    // -> User Statistics
    public UserStatistics getUserStatistics(String username) {
        return customUserDetailsService.getUserStatistics(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Notifications
    public UserNotifications getUserNotifications(String username) {
        return customUserDetailsService.getUserNotifications(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Privacy
    public UserPrivacy getUserPrivacy(String username) {
        return customUserDetailsService.getUserPrivacy(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Profile
    public UserProfile getUserProfile(String username) {
        return customUserDetailsService.getUserProfile(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Social Profiles
    public UserSocialProfiles getUserSocialProfiles(String username) {
        return customUserDetailsService.getUserSocialProfiles(customUserDetailsService.getUserByUsername(username));
    }
}
