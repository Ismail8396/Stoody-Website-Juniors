package com.loam.stoody.service.user;

import com.loam.stoody.components.IAuthenticationFacade;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user.User;
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

    public Boolean didIReportThisUser(String reportedBy, String reportedUser){
        return reportService.doesReportedUserExistByUsername(reportedBy,reportedUser);
    }
}
