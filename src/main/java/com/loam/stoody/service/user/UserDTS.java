package com.loam.stoody.service.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.*;
import com.loam.stoody.model.user.statistics.UserStatistics;
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

    public User getCurrentUser(){
        return customUserDetailsService.getCurrentUser();
    }

    public User getUserByUsername(String username){
        User user = null;
        try {
            user = customUserDetailsService.getUserByUsername(username);
        } catch (UsernameNotFoundException ignore) {
            return null;
        }
        return user;
    }

    // -> Followers / Following
    public List<User> getUserFollowersByUsername(String username){
        return customUserDetailsService.getUserFollowers(customUserDetailsService.getUserByUsername(username));
    }

    public List<User> getUserFollowingsByUsername(String username){
        return customUserDetailsService.getUserFollowings(customUserDetailsService.getUserByUsername(username));
    }

    public Boolean isUserFollowedByMe(String userFrom,
                                      String userTo){
        return customUserDetailsService.isUserFollowedBy(customUserDetailsService.getUserByUsername(userFrom),
                                                  customUserDetailsService.getUserByUsername(userTo));
    }

    // -> Report User
    public Boolean didIReportThisUser(String reportedBy, String reportedUser){
        return reportService.doesReportedUserExistByUsername(reportedBy,reportedUser);
    }

    // -> User Statistics
    public UserStatistics getUserStatistics(String username){
        return customUserDetailsService.getUserStatistics(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Notifications
    public UserNotifications getUserNotifications(String username){
        return customUserDetailsService.getUserNotifications(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Privacy
    public UserPrivacy getUserPrivacy(String username){
        return customUserDetailsService.getUserPrivacy(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Profile
    public UserProfile getUserProfile(String username){
        return customUserDetailsService.getUserProfile(customUserDetailsService.getUserByUsername(username));
    }

    // -> User Social Profiles
    public UserSocialProfiles getUserSocialProfiles(String username){
        return customUserDetailsService.getUserSocialProfiles(customUserDetailsService.getUserByUsername(username));
    }
}
