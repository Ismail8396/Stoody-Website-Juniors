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

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsService.getUserByUsername(username);
    }

    public String getUserDisplayNameByUsername(String username) {
        UserProfile userProfile = getUserProfile(username);
        if (userProfile != null) if (userProfile.getFirstName() != null && userProfile.getLastName() != null)
            if (!userProfile.getFirstName().isBlank() && !userProfile.getLastName().isBlank())
                return userProfile.getFirstName() + " " + userProfile.getLastName();

        try {
            User user = getUserByUsername(username);
            if (user != null) return user.getUsername();
        } catch (UsernameNotFoundException ignore) {}
        return "NULL";
    }

    public String getUserStatusCSSClass(String username) {
        try {
            String userStatus = customUserDetailsService.getUserProfile(getUserByUsername(username)).getUserStatus();
            if (userStatus.equals(UserStatus.Online.toString())) {
                return "avatar-indicators avatar-online";
            } else if (userStatus.equals(UserStatus.Offline.toString())) {
                return "avatar-indicators avatar-offline";
            } else if (userStatus.equals(UserStatus.Away.toString())) {
                return "avatar-indicators avatar-away";
            } else if (userStatus.equals(UserStatus.Busy.toString())) {
                return "avatar-indicators avatar-busy";
            } else {
                return "avatar-indicators avatar-offline";
            }
        } catch (UsernameNotFoundException ignore) {
            return "avatar-indicators avatar-offline";
        }
    }

    // -> Roles
    public boolean hasRole(String username, String role){
        return customUserDetailsService.hasRole(username,role);
    }

    public boolean addRole(String username, String role){
        return customUserDetailsService.addRole(username,role);
    }

    // -> Followers / Following
    public List<User> getUserFollowersByUsername(String username) {
        return customUserDetailsService.getUserFollowers(customUserDetailsService.getUserByUsername(username));
    }

    public List<User> getUserFollowingsByUsername(String username) {
        return customUserDetailsService.getUserFollowings(customUserDetailsService.getUserByUsername(username));
    }

    public Boolean isUserFollowedByMe(String userFrom, String userTo) {
        return customUserDetailsService.isUserFollowedBy(customUserDetailsService.getUserByUsername(userFrom), customUserDetailsService.getUserByUsername(userTo));
    }

    // -> Report User
    public Boolean didIReportThisUser(String reportedBy, String reportedUser) {
        return reportService.doesReportedUserExistByUsername(reportedBy, reportedUser);
    }

    // -> User Statistics
    public UserStatistics getUserStatistics(String username) {
        return customUserDetailsService.getUserStatistics(customUserDetailsService.getUserByUsername(username));
    }

    public String getUserRoleBadgeName(String username) {
        return customUserDetailsService.getUserRoleBadgeName(username);
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
